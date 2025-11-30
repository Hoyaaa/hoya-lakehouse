package kr.ac.nsu.hakbokgs.main.store.kkuihankki.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kr.ac.nsu.hakbokgs.databinding.ActivityKkuiListBinding
import kr.ac.nsu.hakbokgs.main.MainActivity
import kr.ac.nsu.hakbokgs.main.chat.ChatBoardHomeActivity
import kr.ac.nsu.hakbokgs.main.mypage.MypageActivity
import kr.ac.nsu.hakbokgs.main.store.cart.BasketActivity
import kr.ac.nsu.hakbokgs.main.store.order.OrderHistoryActivity

class KkuiListActivity : AppCompatActivity() {
    private val tag: String = "KkuiListActivity"
    private lateinit var binding: ActivityKkuiListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityKkuiListBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // 각 메뉴 버튼 클릭 시 해당 카테고리로 이동
        binding.btnYori.setOnClickListener {
            goToMenuList("food")
        }

        binding.btnDak.setOnClickListener {
            goToMenuList("chicken")
        }

        binding.btnChi.setOnClickListener {
            goToMenuList("chickenmayo")
        }

        binding.btnEum.setOnClickListener {
            goToMenuList("drink")
        }

        // 👇장바구니 버튼
        binding.cartButton.bringToFront() // 맨 앞으로
        binding.cartButton.setOnClickListener {
            val intent = Intent(this, BasketActivity::class.java)
            startActivity(intent)
        }

        // 👇 뒤로가기 버튼
        binding.icoBack.setOnClickListener {
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

    private fun goToMenuList(category: String) {
        Log.i(tag, "${category} 선택")
        val intent = Intent(this, MenuListActivity::class.java)
        intent.putExtra("menuCategory", category)
        startActivity(intent)
    }
}
