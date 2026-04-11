package kr.ac.nsu.hakbokgs.main.store.taesan.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kr.ac.nsu.hakbokgs.databinding.ActivityTaesanListBinding
import kr.ac.nsu.hakbokgs.main.MainActivity
import kr.ac.nsu.hakbokgs.main.chat.ChatBoardHomeActivity
import kr.ac.nsu.hakbokgs.main.mypage.MypageActivity
import kr.ac.nsu.hakbokgs.main.store.cart.BasketActivity
import kr.ac.nsu.hakbokgs.main.store.order.OrderHistoryActivity

class TaesanListActivity : AppCompatActivity() {
    private val tag = "TaesanListActivity"
    private lateinit var binding: ActivityTaesanListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTaesanListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 👇 각 메뉴 버튼 클릭 시 해당 카테고리로 이동
        binding.taesanKimchijjim.setOnClickListener {
            goToMenuList("kimchijjim")
        }
        binding.taesanSide.setOnClickListener {
            goToMenuList("side")
        }

        // 👇 장바구니
        binding.cartButton.bringToFront()
        binding.cartButton.setOnClickListener {
            val intent = Intent(this, BasketActivity::class.java)
            startActivity(intent)
        }


        // 👇 뒤로 가기
        binding.icoBack.setOnClickListener {
            finish()
        }


        // 👇 하단 바
        binding.mainHome.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.mainChat.setOnClickListener {
            val intent = Intent(this, ChatBoardHomeActivity::class.java)
            startActivity(intent)
        }

        binding.mainList.setOnClickListener {
            val intent = Intent(this, OrderHistoryActivity::class.java)
            startActivity(intent)
        }

        binding.mainMypage.setOnClickListener {
            val intent = Intent(this, MypageActivity::class.java)
            startActivity(intent)
        }
    }

    private fun goToMenuList(category: String) {
        Log.i(tag, "${category} 선택")
        val intent = Intent(this, MenuListActivity::class.java)
        intent.putExtra("menuCategory", category)
        startActivity(intent)
    }
}