package com.example.imagehouseholdbook

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Locale

class DetailActivity : AppCompatActivity() {
    private val db = Firebase.firestore
    private val auth = Firebase.auth

    private var docIdList: ArrayList<String> = arrayListOf()
    private var currentIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val userEmail = auth.currentUser?.email

        // 1. Intent 데이터
        docIdList = intent.getStringArrayListExtra("DOC_ID_LIST") ?: arrayListOf()
        currentIndex = intent.getIntExtra("CURRENT_INDEX", 0)

        if (docIdList.isNotEmpty() && userEmail != null) {
            loadDetailData(userEmail, docIdList[currentIndex])
        } else {
            Toast.makeText(this, "데이터 없음", Toast.LENGTH_SHORT).show()
            finish()
        }

        // 2. 이전 버튼 (ID 수정: btnprev → btn_prev)
        findViewById<ImageView>(R.id.btn_prev).setOnClickListener {
            if (currentIndex > 0) {
                currentIndex--
                if (userEmail != null) {
                    loadDetailData(userEmail, docIdList[currentIndex])
                }
            } else {
                Toast.makeText(this, "첫 번째 항목입니다", Toast.LENGTH_SHORT).show()
            }
        }

        // 3. 다음 버튼 (ID 수정: btnnext → btn_next)
        findViewById<ImageView>(R.id.btn_next).setOnClickListener {
            if (currentIndex < docIdList.size - 1) {
                currentIndex++
                if (userEmail != null) {
                    loadDetailData(userEmail, docIdList[currentIndex])
                }
            } else {
                Toast.makeText(this, "마지막 항목입니다", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadDetailData(email: String, docId: String) {
        db.collection("user").document(email)
            .collection("imagehouseholdbook").document(docId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val timestamp = document.getTimestamp("purchaseDate")
                    val dateStr = if (timestamp != null) {
                        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(timestamp.toDate())
                    } else "-"

                    // 핵심 6개 컬럼
                    findViewById<TextView>(R.id.tvStoreName).text = document.getString("storeName") ?: "-"
                    findViewById<TextView>(R.id.tvPurchaseDate).text = dateStr
                    findViewById<TextView>(R.id.tvProductName).text = document.getString("productName") ?: "-"

                    val unitPrice = document.getLong("unitPrice") ?: 0L
                    findViewById<TextView>(R.id.tvUnitPrice).text = String.format("%,d원", unitPrice)

                    val receivedQty = document.getLong("receivedQty") ?: 0L
                    findViewById<TextView>(R.id.tvReceivedQty).text = receivedQty.toString()

                    val totalAmount = document.getLong("totalAmount") ?: 0L
                    findViewById<TextView>(R.id.tvTotalAmount).text = String.format("%,d원", totalAmount)

                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "상세 데이터 로드 실패", Toast.LENGTH_SHORT).show()
            }
    }
}
