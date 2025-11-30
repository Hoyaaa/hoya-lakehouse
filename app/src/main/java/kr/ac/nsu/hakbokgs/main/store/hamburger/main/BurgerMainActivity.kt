package kr.ac.nsu.hakbokgs.main.store.hamburger.main

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import kr.ac.nsu.hakbokgs.R
import kr.ac.nsu.hakbokgs.databinding.ActivityBurgerMainBinding
import kr.ac.nsu.hakbokgs.main.MainActivity
import kr.ac.nsu.hakbokgs.main.chat.ChatBoardHomeActivity
import kr.ac.nsu.hakbokgs.main.mypage.MypageActivity
import kr.ac.nsu.hakbokgs.main.store.cart.BasketActivity
import kr.ac.nsu.hakbokgs.main.store.order.OrderHistoryActivity

class BurgerMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBurgerMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBurgerMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 👇 Burger List 화면으로 이동
        val intent = Intent(this, BurgerListActivity::class.java)
        binding.btnBurgerOrder.setOnClickListener {
            startActivity(intent)
        }
        binding.btnBurgerShadow.setOnClickListener {
            startActivity(intent)
        }

        // 👇 장바구니
        binding.cartButton.setOnClickListener {
            val intent = Intent(this, BasketActivity::class.java)
            startActivity(intent)
        }


        // 👇 이전 화면으로 가기_Activity Main
        binding.btnBack.setOnClickListener{
            finish()
        }

        // 👇 하단 바
        binding.mainHome.setOnClickListener {
            val intent= Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.mainChat.setOnClickListener {
            val intent=Intent(this, ChatBoardHomeActivity::class.java)
            startActivity(intent)
        }

        binding.mainList.setOnClickListener {
            val intent=Intent(this, OrderHistoryActivity::class.java)
            startActivity(intent)
        }

        binding.mainMypage.setOnClickListener {
            val intent=Intent(this, MypageActivity::class.java)
            startActivity(intent)
        }
    }

}