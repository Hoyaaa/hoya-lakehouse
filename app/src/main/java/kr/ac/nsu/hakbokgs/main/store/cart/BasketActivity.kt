package kr.ac.nsu.hakbokgs.main.store.cart

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import kr.ac.nsu.hakbokgs.databinding.ActivityBasketBinding
import kr.ac.nsu.hakbokgs.main.store.cart.data.BasketViewModel
import java.text.DecimalFormat

class BasketActivity : AppCompatActivity() {
    private val TAG = "BasketActivity"
    private lateinit var binding: ActivityBasketBinding
    private lateinit var basketViewModel: BasketViewModel
    private lateinit var basketAdapter: BasketAdapter
    private lateinit var userId: String

    private var currentTotalPrice = 0
    private var currentTotalAmount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBasketBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ✅ 로그인한 사용자 이메일 기반 ID 사용
        userId = FirebaseAuth.getInstance().currentUser?.email ?: return

        // ViewModel 초기화
        basketViewModel = ViewModelProvider(this)[BasketViewModel::class.java]

        // ✅ Firestore 장바구니 데이터 로드
        basketViewModel.loadBasketData(userId)

        // RecyclerView 설정
        binding.rvEachStore.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@BasketActivity)
        }

        // ✅ LiveData로 장바구니 항목 옵저빙 → UI에 실시간 반영
        basketViewModel.basketList.observe(this) { list ->
            basketAdapter = BasketAdapter(this, userId, basketViewModel, this)
            binding.rvEachStore.adapter = basketAdapter
        }

        // 총 주문 금액 옵저빙
        val formatter = DecimalFormat("#,###")
        basketViewModel.totalPriceAll.observe(this) { total ->
            currentTotalPrice = total
            binding.totalPrice.text = formatter.format(total) + "원"
        }

        // 총 주문 개수 옵저빙
        basketViewModel.totalAmount.observe(this) { amount ->
            currentTotalAmount = amount
            binding.orderButton.text = "결제하기 (${amount}개)"
        }

        // 주문하기 버튼 클릭 시
        binding.orderButton.setOnClickListener {
            if(basketViewModel.basketList.value?.isNotEmpty() == true){
                val intent = Intent(this, PayActivity::class.java).apply {
                    Log.i("test","결제창 이동")
                    putExtra("total_price", currentTotalPrice)
                    putExtra("total_amount", currentTotalAmount)
                    putExtra("total_data", ArrayList(basketViewModel.basketList.value ?: emptyList()))
                }
                startActivity(intent)
            }else{
                Toast.makeText(this, "장바구니가 비어 있습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        // 뒤로 가기
        binding.btnBack.setOnClickListener {
            finish()
        }
    }
}
