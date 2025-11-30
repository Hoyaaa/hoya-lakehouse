package kr.ac.nsu.hakbokgs.main.store.cart

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kr.ac.nsu.hakbokgs.R
import kr.ac.nsu.hakbokgs.main.store.cart.data.BasketMenu
import kr.ac.nsu.hakbokgs.main.store.order.OrderDetailCompleteActivity

class LoadingActivity : AppCompatActivity() {
    private val tag = "LoadingActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading_page)

        // 결제 완료 후 전달받은 주문 정보
        val orderList = intent.getSerializableExtra("order_data") as? ArrayList<BasketMenu>
        val paymentMethod = intent.getStringExtra("paymentMethod")
        val orderId = intent.getStringExtra("order_id")

        Log.i(tag, "로딩 시작 - 결제수단: $paymentMethod, 주문수: ${orderList?.size}")

        // 3초 후 ReceiptActivity로 이동
        Handler().postDelayed({
            val intent = Intent(this@LoadingActivity, OrderDetailCompleteActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("order_id", orderId)
            startActivity(intent)
            finish()
        }, 3000)
    }
}
