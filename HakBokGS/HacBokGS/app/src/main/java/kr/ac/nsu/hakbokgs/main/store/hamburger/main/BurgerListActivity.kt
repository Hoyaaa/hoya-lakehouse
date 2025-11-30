package kr.ac.nsu.hakbokgs.main.store.hamburger.main

import android.content.Intent
import android.os.Bundle
import android.util.Log

import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import kr.ac.nsu.hakbokgs.R
import kr.ac.nsu.hakbokgs.databinding.ActivityBurgerListBinding
import kr.ac.nsu.hakbokgs.main.MainActivity
import kr.ac.nsu.hakbokgs.main.chat.ChatBoardHomeActivity
import kr.ac.nsu.hakbokgs.main.mypage.MypageActivity
import kr.ac.nsu.hakbokgs.main.store.cart.BasketActivity
import kr.ac.nsu.hakbokgs.main.store.order.OrderHistoryActivity

class BurgerListActivity : AppCompatActivity() {
    private val Tag: String = "BurgerListActivity"
    private lateinit var binding: ActivityBurgerListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBurgerListBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val intent = Intent(this, MenuListActivity::class.java)

        // 버거 Menu List로 이동
        binding.btnListBurger.setOnClickListener {
            Log.i(Tag, "햄버거 선택")
            intent.putExtra("menuCategory", "burger")  // ✅ 여기 추가
            startActivity(intent)
        }

        // 치킨 Menu List로 이동
        binding.btnListChicken.setOnClickListener {
            Log.i(Tag, "치킨 선택")
            intent.putExtra("menuCategory", "chicken") // ✅ 여기 추가
            startActivity(intent)
        }

        // 사이드 Menu List로 이동
        binding.btnListSide.setOnClickListener {
            Log.i(Tag, "사이드 선택")
            intent.putExtra("menuCategory", "side")    // ✅ 여기도 필요 시 추가
            startActivity(intent)

        }

        // 👇 장바구니
        binding.cartButton.bringToFront() // 맨 앞으로
        binding.cartButton.setOnClickListener {
            val intent = Intent(this, BasketActivity::class.java)
            startActivity(intent)
        }


        // 👇 이전 화면으로 가기
        binding.btnBack.setOnClickListener {
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