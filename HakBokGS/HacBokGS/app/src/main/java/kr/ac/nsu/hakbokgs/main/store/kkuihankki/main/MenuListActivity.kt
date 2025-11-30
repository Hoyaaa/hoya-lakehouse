package kr.ac.nsu.hakbokgs.main.store.kkuihankki.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import kr.ac.nsu.hakbokgs.databinding.ActivityKkuiMenuListBinding
import kr.ac.nsu.hakbokgs.main.store.kkuihankki.db.Menu
import kr.ac.nsu.hakbokgs.main.store.kkuihankki.main.food.FoodMenuChooseFragment
import kr.ac.nsu.hakbokgs.main.store.kkuihankki.main.chicken.ChickenMenuChooseFragment
import kr.ac.nsu.hakbokgs.main.store.kkuihankki.main.chickenmayo.ChickenmayoMenuChooseFragment
import kr.ac.nsu.hakbokgs.main.store.kkuihankki.main.drink.DrinkMenuChooseFragment
import com.google.firebase.firestore.FirebaseFirestore
import kr.ac.nsu.hakbokgs.main.MainActivity
import kr.ac.nsu.hakbokgs.main.chat.ChatBoardHomeActivity
import kr.ac.nsu.hakbokgs.main.mypage.MypageActivity
import kr.ac.nsu.hakbokgs.main.store.cart.BasketActivity
import kr.ac.nsu.hakbokgs.main.store.order.OrderHistoryActivity

class MenuListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityKkuiMenuListBinding
    private val TAG = "MenuListActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKkuiMenuListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val menuCategory = intent.getStringExtra("menuCategory") ?: "food"

        val adapter = MenuAdapter { menu ->
            val fragment = when (menuCategory) {
                "food" -> FoodMenuChooseFragment()
                "chicken" -> ChickenMenuChooseFragment()
                "chickenmayo" -> ChickenmayoMenuChooseFragment()
                "drink" -> DrinkMenuChooseFragment()
                else -> null
            }

            fragment?.arguments = Bundle().apply {
                putParcelable("menu", menu)
            }

            fragment?.show(supportFragmentManager, "MenuChooseFragment")
        }

        binding.kkuiMenuRecycleView.layoutManager = GridLayoutManager(this, 2)
        binding.kkuiMenuRecycleView.adapter = adapter

        // 🔍 Firestore 쿼리 + 로깅
        FirebaseFirestore.getInstance()
            .collection("store").document("kkuihankki")
            .collection(menuCategory)
            .get() // 🔥 조건 제거: .whereEqualTo("SalesStatus", "sell") 제거
            .addOnSuccessListener { result ->
                val menuList = mutableListOf<Menu>()
                for (doc in result) {
                    Log.d(TAG, "📄 문서 ID: ${doc.id}")
                    Log.d(TAG, "📦 원본 데이터: ${doc.data}")

                    try {
                        val menu = doc.toObject(Menu::class.java)
                        if (menu == null) {
                            Log.w(TAG, "⚠️ Menu 객체 변환 실패 - ID: ${doc.id}")
                            continue
                        }

                        menu.documentId = doc.id
                        Log.d(TAG, "✅ Menu 변환 성공: $menu")

                        if (menu.SalesStatus == "sell") {
                            menuList.add(menu)
                        } else {
                            Log.i(TAG, "🚫 제외된 메뉴: ${menu.id} (SalesStatus = ${menu.SalesStatus})")
                        }

                    } catch (e: Exception) {
                        Log.e(TAG, "❌ 예외 발생 - ID: ${doc.id}", e)
                    }
                }

                Log.i(TAG, "$menuCategory 최종 추가된 메뉴: ${menuList.size}개")
                adapter.submitList(menuList)
            }
            .addOnFailureListener {
                Log.e(TAG, "🔥 Firestore에서 메뉴 로딩 실패", it)
            }

        // 👇 장바구니 버튼
        binding.cartButton.bringToFront() // 맨 앞으로
        binding.cartButton.setOnClickListener { 
            val intent= Intent(this, BasketActivity::class.java)
            startActivity(intent)
        }
        
        // 👇 뒤로 가기 버튼
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
