package kr.ac.nsu.hakbokgs.main.store.order

import android.content.Intent
import android.graphics.Color
import android.icu.text.DecimalFormat
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import kr.ac.nsu.hakbokgs.R
import kr.ac.nsu.hakbokgs.main.MainActivity
import kr.ac.nsu.hakbokgs.main.chat.ChatBoardHomeActivity
import kr.ac.nsu.hakbokgs.main.mypage.MypageActivity
import kr.ac.nsu.hakbokgs.main.store.order.data.OrderViewModel
import java.util.Locale

class OrderDetailCompleteActivity : AppCompatActivity() {
    private val tag = "OrderDetailCompleteActivity"

    private lateinit var orderViewModel: OrderViewModel
    private lateinit var mainRecyclerView: RecyclerView
    private lateinit var orderDetailAdapter: OrderDetailAdapter
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_detail_complete)

        val orderNum: TextView = findViewById(R.id.orderNumberTextView)
        var finalCompleteText: TextView = findViewById(R.id.completeTextView)
        val orderDate: TextView = findViewById(R.id.dateTextView)
        val totalPriceText: TextView = findViewById(R.id.totalPriceTextview)
        val payment: TextView = findViewById(R.id.paymentTextView)
        val deleteButton: Button = findViewById(R.id.deleteButton)

        // ✅ 로그인한 사용자 이메일 기반 ID 사용
        userId = FirebaseAuth.getInstance().currentUser?.email ?: return

        val orderId = intent.getStringExtra("order_id") ?: return

        orderViewModel = ViewModelProvider(this)[OrderViewModel::class.java] // ViewModel 생성

        // 총 조리 상태 초기화
        var finalComplete = false

        // orderId의 데이터 가져오기
        orderViewModel.listenOrderDataByOrderId(userId, orderId)
        Log.i(tag, "가져온 데이터: ${orderId}")
        orderViewModel.orderDataByOrderId.observe(this) { orderData ->
            Log.d(tag, "$orderData")
            // 주문 번호
            orderNum.text = "${orderData.orderNum}번"
            // 결제 수단
            payment.text = "${orderData.payment}"

            // 총 조리 상태
            finalComplete = orderViewModel.finalComplete.value?.get(orderId) ?: false
            if (finalComplete) {
                finalCompleteText.text = "조리 완료"
                finalCompleteText.setTextColor(Color.BLUE)
            } else {
                finalCompleteText.text = "조리 중"
                finalCompleteText.setTextColor(Color.RED)
            }

            // 총 결제 금액
            val totalPrice = orderViewModel.totalPrice.value?.get(orderId) ?: 0
            val formatPrice = DecimalFormat("#,###")
            totalPriceText.text = "${formatPrice.format(totalPrice)}원"

            // 주문 날짜
            val rawDate = orderData.orderDate?.toDate()
            val formatDate = SimpleDateFormat("yyyy/MM/dd a HH:mm", Locale.KOREA)
            orderDate.text = "${formatDate.format(rawDate)}"
        }


        /* RecyclerView */
        // 외부 recyclerView 연결
        mainRecyclerView = findViewById(R.id.rv_store_order)
        //mainRecyclerView.setHasFixedSize(true)

        // recyclerView 형태
        mainRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        // recyclerView에 데이터 전달
        orderDetailAdapter = OrderDetailAdapter(this, orderViewModel, this)
        mainRecyclerView.adapter = orderDetailAdapter


        // 삭제 버튼
        // 조리가 완료된 주문만 삭제 가능
        deleteButton.setOnClickListener {
            if (finalComplete) {
                AlertDialog.Builder(this).setMessage("정말 삭제하시겠습니까?")
                    .setPositiveButton("삭제") { dialog, which ->
                        orderViewModel.deleteItemFromFirestore(userId, orderId) {
                            Toast.makeText(this, "삭제되었습니다.", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    }
                    .setNegativeButton("취소", null).show()
            } else {
                Toast.makeText(this, "주문이 완료되지 않았습니다.", Toast.LENGTH_SHORT).show()
            }
        }


        // 뒤로 가기 버튼
        val backButton: ImageButton = findViewById(R.id.btn_back)
        backButton.setOnClickListener { finish() }


        // 홈 버튼
        findViewById<LinearLayout>(R.id.main_home).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        // ✅ 게시판 진입 버튼 (게시글 기능)
        val btnChat = findViewById<LinearLayout>(R.id.main_chat)
        btnChat.setOnClickListener { v: View? ->
            val intent = Intent(
                this@OrderDetailCompleteActivity,
                ChatBoardHomeActivity::class.java
            )
            startActivity(intent)
        }

        // ✅ 주문내역 진입 버튼
        val btnList = findViewById<LinearLayout>(R.id.main_list)
        btnList.setOnClickListener { v: View? ->
            val intent = Intent(
                this@OrderDetailCompleteActivity,
                OrderHistoryActivity::class.java
            )
            startActivity(intent)
        }

        // 마이페이지 버튼 클릭 → MypageActivity로 이동
        val btnMypage = findViewById<LinearLayout>(R.id.main_mypage)
        btnMypage.setOnClickListener {
            val intent = Intent(
                this@OrderDetailCompleteActivity,
                MypageActivity::class.java
            )
            startActivity(intent)
        }
    }

}