package kr.ac.nsu.hakbokgs.main.store.kkuihankki.main

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kr.ac.nsu.hakbokgs.databinding.ActivityKkuiMainBinding
import kr.ac.nsu.hakbokgs.main.MainActivity
import kr.ac.nsu.hakbokgs.main.chat.ChatBoardHomeActivity
import kr.ac.nsu.hakbokgs.main.mypage.MypageActivity
import kr.ac.nsu.hakbokgs.main.store.cart.BasketActivity
import kr.ac.nsu.hakbokgs.main.store.order.OrderHistoryActivity

class KkuiMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityKkuiMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKkuiMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 👇 메인 버튼 클릭 시 메뉴 리스트 화면으로 이동
        binding.kkuiMain.setOnClickListener {
            val intent = Intent(this, KkuiListActivity::class.java)
            startActivity(intent)
        }

        // 👇 장바구니 버튼
        binding.cartButton.bringToFront() // 맨 앞으로
        binding.cartButton.setOnClickListener {
            val intent = Intent(this, BasketActivity::class.java)
            startActivity(intent)
        }


        // 👇 뒤로가기
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
}
