package kr.ac.nsu.hakbokgs.main.store.hamburger.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import kr.ac.nsu.hakbokgs.databinding.ActivityBurgerMenuListBinding
import kr.ac.nsu.hakbokgs.main.MainActivity
import kr.ac.nsu.hakbokgs.main.chat.ChatBoardHomeActivity
import kr.ac.nsu.hakbokgs.main.mypage.MypageActivity
import kr.ac.nsu.hakbokgs.main.store.hamburger.db.Menu
import kr.ac.nsu.hakbokgs.main.store.hamburger.main.burger.BurgerMenuChooseFragment
import kr.ac.nsu.hakbokgs.main.store.hamburger.main.chicken.ChickenMenuChooseFragment
import kr.ac.nsu.hakbokgs.main.store.hamburger.main.side.SideMenuOneSizeChooseFragment
import kr.ac.nsu.hakbokgs.main.store.cart.BasketActivity
import kr.ac.nsu.hakbokgs.main.store.hamburger.main.side.SideMenuTwoSizesFragment
import kr.ac.nsu.hakbokgs.main.store.order.OrderHistoryActivity

class MenuListActivity : AppCompatActivity() {
    private val TAG = "MenuListActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityBurgerMenuListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val menuCategory = intent.getStringExtra("menuCategory") ?: "burger"

        val adapter = MenuAdapter { menu ->
            val fragment = when (menuCategory) {
                "chicken" -> ChickenMenuChooseFragment()
                "side" -> if (menu.size?.size == 2) {
                    SideMenuTwoSizesFragment()
                } else {
                    SideMenuOneSizeChooseFragment()
                }
                else -> BurgerMenuChooseFragment()
            }

            fragment.arguments = Bundle().apply {
                putSerializable("menu", menu)
            }
            fragment.show(supportFragmentManager, "${menuCategory.uppercase()} MENU CHOOSE")
        }

        binding.menuRecycleView.layoutManager = GridLayoutManager(this, 2)
        binding.menuRecycleView.adapter = adapter

        // Firestore에서 해당 카테고리의 메뉴 로그
        FirebaseFirestore.getInstance()
            .collection("store").document("hamburger")
            .collection(menuCategory)
            .get()
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


        // 👇 뒤로 가기
        binding.btnBack.setOnClickListener {
            finish()
        }

        // 👇 장바구니
        binding.cartButton.setOnClickListener {
            val intent = Intent(this, BasketActivity::class.java)
            startActivity(intent)
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

}
