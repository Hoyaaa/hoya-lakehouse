package com.example.imagehouseholdbook

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private val db = Firebase.firestore
    private val auth = Firebase.auth
    private lateinit var adapter: HouseholdAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.rv_household_list)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = HouseholdAdapter(emptyList()) { group ->
            val intent = Intent(this, DetailActivity::class.java)
            intent.putStringArrayListExtra("DOC_ID_LIST", group.itemDocIds)
            intent.putExtra("CURRENT_INDEX", 0)
            startActivity(intent)
        }
        recyclerView.adapter = adapter

        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        findViewById<TextView>(R.id.tv_main_date).text = today

        findViewById<View>(R.id.layout_camera_btn).setOnClickListener {
            startActivity(Intent(this, CameraActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        val currentUser = auth.currentUser
        if (currentUser != null && currentUser.email != null) {
            loadAndGroupData(currentUser.email!!)
        }
    }

    // 6컬럼용: (구매날짜 + 가맹점명) 기준으로 그룹핑
    private fun loadAndGroupData(email: String) {
        db.collection("user").document(email)
            .collection("imagehouseholdbook")
            .orderBy("purchaseDate", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val rawItemList = mutableListOf<HouseholdData>()

                for (document in querySnapshot.documents) {
                    val timestamp = document.getTimestamp("purchaseDate")
                    val dateString = if (timestamp != null) {
                        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(timestamp.toDate())
                    } else ""

                    val data = HouseholdData(
                        docId = document.id,
                        date = dateString,
                        storeName = document.getString("storeName") ?: "",
                        productName = document.getString("productName") ?: "",
                        unitPrice = document.getLong("unitPrice") ?: 0L,
                        receivedQty = document.getLong("receivedQty") ?: 0L,
                        totalAmount = document.getLong("totalAmount") ?: 0L
                    )
                    rawItemList.add(data)
                }

                // (date + storeName)로 그룹화
                val groupedMap = rawItemList.groupBy { item ->
                    "${item.date}__${item.storeName}"
                }

                val displayList = mutableListOf<HouseholdGroup>()
                for ((_, items) in groupedMap) {
                    val firstItem = items[0]
                    val totalSum = items.sumOf { it.totalAmount }
                    val count = items.size

                    val title = if (count == 1) {
                        firstItem.productName
                    } else {
                        "${firstItem.productName} 외 ${count - 1}건"
                    }

                    val docIds = ArrayList(items.map { it.docId })

                    displayList.add(
                        HouseholdGroup(
                            date = firstItem.date,
                            place = firstItem.storeName,   // 화면의 "장소" 칸에 가맹점명 표시
                            totalPrice = totalSum,
                            representativeName = title,
                            itemDocIds = docIds
                        )
                    )
                }

                displayList.sortByDescending { it.date }
                adapter.updateData(displayList)
            }
            .addOnFailureListener {
                Toast.makeText(this, "데이터 로드 실패", Toast.LENGTH_SHORT).show()
            }
    }
}
