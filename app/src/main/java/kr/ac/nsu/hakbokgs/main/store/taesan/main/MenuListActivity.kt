package kr.ac.nsu.hakbokgs.main.store.taesan.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import kr.ac.nsu.hakbokgs.databinding.ActivityTaesanMenuListBinding
import kr.ac.nsu.hakbokgs.main.MainActivity
import kr.ac.nsu.hakbokgs.main.chat.ChatBoardHomeActivity
import kr.ac.nsu.hakbokgs.main.mypage.MypageActivity
import kr.ac.nsu.hakbokgs.main.store.cart.BasketActivity
import kr.ac.nsu.hakbokgs.main.store.order.OrderHistoryActivity
import kr.ac.nsu.hakbokgs.main.store.taesan.db.Menu
import kr.ac.nsu.hakbokgs.main.store.taesan.db.Size
import kr.ac.nsu.hakbokgs.main.store.taesan.db.SizeOption
import kr.ac.nsu.hakbokgs.main.store.taesan.main.kimchijjim.KimchiJjimMenuChooseFragment
import kr.ac.nsu.hakbokgs.main.store.taesan.main.noSize.NoSizeMenuChooseFragment

class MenuListActivity : AppCompatActivity() {
    private val TAG = "TaesanMenuListActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityTaesanMenuListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 메뉴 카테고리 가져오기
        val menuCategory = intent.getStringExtra("menuCategory") ?: "kimchijjim"

        // 프레그먼트 연결
        val adapter = MenuAdapter { menu ->
            val bundle = Bundle().apply {
                putParcelable("menu", menu)
            }

            val fragment = if (menuCategory == "side" || menu.id == "김치갱시기") { // '김치갱시기' 또는 사이드
                NoSizeMenuChooseFragment()
            } else {
                KimchiJjimMenuChooseFragment()
            }
            fragment.arguments = bundle
            fragment.show(supportFragmentManager, "MenuChooseFragment")
        }

        binding.taesanMenuRecycleView.layoutManager = GridLayoutManager(this, 2)
        binding.taesanMenuRecycleView.adapter = adapter

        // 📁 Firestore 쿼리 + 로깅
        FirebaseFirestore.getInstance()
            .collection("store").document("taesankimchijjim")
            .collection(menuCategory)
            .get()
            .addOnSuccessListener { result ->
                val menuList = mutableListOf<Menu>()
                for (doc in result) {
                    Log.d(TAG, "📄 문서 ID: ${doc.id}")
                    Log.d(TAG, "📦 원본 데이터: ${doc.data}")

                    try {
                        // val menu = doc.toObject(Menu::class.java)
                        // 수동 파싱
                        val data = doc.data

                        val sizeMap = data["size"] as? Map<*, *>
                        val size = Size()

                        for ((key, value) in sizeMap.orEmpty()) {
                            val valueMap = value as? Map<*, *>
                            val price = valueMap?.get("price")?.toString()
                            val keyStr=key.toString().trim()

                            when (keyStr) {
                                "baekban" -> size.baekban = SizeOption(price)
                                "jeongsik" -> size.jeongsik = SizeOption(price)
                                "basic" -> size.basic = SizeOption(price)
                            }
                        }
                        Log.d("test", "${size}")

                        val testSize = Size()
                        testSize.baekban = SizeOption("1234")

                        val menu = Menu(
                            id = data["id"] as? String,
                            description = data["description"] as? String,
                            imagePath = data["imagePath"] as? String,
                            ingredient = (data["ingredient"] as? List<*>)?.mapNotNull { it?.toString() },
                            SalesStatus = data["SalesStatus"] as? String,
                            size = size,
                            documentId = doc.id
                        )

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

        // 👇 장바구니
        binding.cartButton.bringToFront()
        binding.cartButton.setOnClickListener {
            val intent = Intent(this, BasketActivity::class.java)
            startActivity(intent)
        }


        // 👇 뒤로 가기
        binding.btnBack.setOnClickListener {
            finish()
        }

        // 👇 하단바
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