package kr.ac.nsu.hakbokgs.main.store.cart

import android.content.Intent
import android.icu.text.DecimalFormat
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.SetOptions
import kr.ac.nsu.hakbokgs.R
import kr.ac.nsu.hakbokgs.main.store.cart.data.BasketMenu
import kr.ac.nsu.hakbokgs.main.store.cart.data.OrderNumManage
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kr.ac.nsu.hakbokgs.main.store.popup.MultiStoreCookingWatcher

import kr.ac.nsu.hakbokgs.main.store.order.data.Option


class PayActivity : AppCompatActivity() {
    private val TAG = "PayActivity"
    private lateinit var radioGroup: RadioGroup
    private var selectedPayment: String = "" // 선택한 결제수단
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        // ✅ 로그인한 사용자 이메일 기반 ID 사용
        userId = FirebaseAuth.getInstance().currentUser?.email ?: return

        // 주문 내역
        val basketList = intent.getSerializableExtra("total_data") as? ArrayList<BasketMenu>
        Log.i(TAG, "결제 대기 중: $basketList")

        // 총 주문 금액
        val formatPrice = DecimalFormat("#,###")
        val totalPrice = intent.getIntExtra("total_price", 0)
        val totalPriceView = findViewById<TextView>(R.id.total_price)
        totalPriceView.text = "${formatPrice.format(totalPrice)}원 결제하기 "

        // 총 주문 개수
        val totalAmount = intent.getIntExtra("total_amount", 0)
        val totalAmountView = findViewById<TextView>(R.id.total_amount)
        totalAmountView.text = "(${totalAmount}개)"

        radioGroup = findViewById(R.id.paymentGroup)

        // 라디오 버튼 선택 이벤트
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            selectedPayment = when (checkedId) {
                R.id.radioToss -> "토스"
                R.id.radioKakao -> "카카오페이"
                R.id.radioNaver -> "네이버페이"
                else -> ""
            }
        }

        // 결제하기 버튼 클릭
        val btnPay = findViewById<LinearLayout>(R.id.btn_pay_money)
        btnPay.setOnClickListener {
            if (selectedPayment.isEmpty()) {
                Toast.makeText(this, "결제 수단을 선택하세요!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "$selectedPayment 로 결제 진행 중...", Toast.LENGTH_SHORT).show()

                // orderId = docId
                val orderId = SimpleDateFormat("yyyyMMdd_HHmm", Locale.getDefault()).format(Date())

                saveOrderToFireStore(basketList, userId, orderId)
                Toast.makeText(this, "결제 완료! 주문이 저장되었습니다.", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, LoadingActivity::class.java)
                intent.putExtra("order_data", basketList)
                intent.putExtra("paymentMethod", selectedPayment)
                intent.putExtra("order_id", orderId)
                startActivity(intent)
                finish()
            }
        }

        // 뒤로 가기
        val adBackButton = findViewById<ImageView>(R.id.btn_back)
        adBackButton.setOnClickListener { finish() }

    }


    // Firestore에 데이터 저장
    private fun saveOrderToFireStore(
        basketList: ArrayList<BasketMenu>?,
        userId: String,
        orderId: String
    ) {
        if (basketList == null || basketList.isEmpty) {
            Toast.makeText(this, "장바구니가 비어 있습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        // 주문번호 가져오기
        OrderNumManage.getOrderNum(object : OrderNumManage.OnOrderNumListener {
            override fun onSuccess(newNum: Int) {
                Log.i(TAG, "주문번호: $newNum, 주문자: $userId")
                val data = regroupOrderData(basketList, newNum, selectedPayment)
                updateOrderDataInFirestore(userId, orderId, data) // 사용자 주문 데이터 저장
                updateStoreDataInFirestore(data) // 가게별 데이터 저장
            }

            override fun onFailure(e: Exception?) {
                Log.e(TAG, "주문번호 가져오기 실패", e)
            }
        })

    }

    /* 데이터 형태 가공 */
    // 메뉴를 가게를 기준으로 묶어 리스트로 저장 _사용자
    private fun regroupOrderData(
        orderList: MutableList<BasketMenu>, orderNum: Int, payment: String?
    ): Map<String, Any> {
        val groupByStoreMap = mutableMapOf<String, Any>()

        groupByStoreMap["orderNumber"] = orderNum
        groupByStoreMap["paymentMethod"] = payment ?: ""
        groupByStoreMap["timestamp"] = Timestamp.now()

        for (item in orderList) {
            val storeName: String? = item.store

            val orderMenu = hashMapOf(
                "menu" to item.menu,
                "menuPrice" to item.menuPrice,
                "amount" to item.amount,
                "optionList" to item.optionList?.map {
                    mapOf(
                    "optionName" to it.optionName,
                    "optionPrice" to it.optionPrice
                    )
                }
            )

            val storeData = groupByStoreMap.getOrPut(storeName.toString()) {
                hashMapOf(
                    "complete" to false,
                    "menuList" to mutableListOf<HashMap<String, Any>>(),
                    "popupShown" to false
                )
            } as HashMap<String, Any>

            val menuList = storeData["menuList"] as MutableList<HashMap<String, Any>>
            menuList.add(orderMenu as HashMap<String, Any>)
        }
        Log.i(TAG, "데이터 가공 완료")
        return groupByStoreMap

    }

    /* Firestore에 데이터 넣기 */
    // 데이터를 firestore order에 저장
    // 장바구니 데이터는 삭제
    private fun updateOrderDataInFirestore(
        userId: String, docId: String, groupByStoreMap: Map<String, Any>
    ) {
        val db = FirebaseFirestore.getInstance()

        val docRef = db.collection("users").document(userId)
            .collection("orders").document(docId)

        docRef.set(groupByStoreMap)
            .addOnSuccessListener(OnSuccessListener { unused: Void? ->
                Log.i(TAG, "주문 정보 저장 성공")

                // ✅ 결제 완료 시점 → 감지 시작
                MultiStoreCookingWatcher.startWatching(userId, docId)

                // 장바구니 데이터 삭제
                val basketRef = db.collection("users").document(userId)
                    .collection("basket")
                basketRef.get()
                    .addOnSuccessListener { queryDocumentSnapshots ->
                        for (doc in queryDocumentSnapshots!!) {
                            doc.reference.delete()
                        }
                    }.addOnFailureListener { e ->
                        Log.e(TAG, "장바구니 삭제 실패")
                    }
            })

    }

    // 각 가게에 주문 데이터 저장
    private fun updateStoreDataInFirestore(data: Map<String, Any>) {
        val orderNum = data["orderNumber"]?.toString() ?: return

        val db = FirebaseFirestore.getInstance()

        val batch = db.batch() // 여러 doc에 작성

        for ((key, value) in data) {
            if (key == "orderNumber" || key == "timestamp") continue

            val storeName = key
            val storeOrder = value as? Map<*, *> ?: continue
            val storeMenuList = storeOrder["menuList"] as? List<*> ?: continue

            val menuList = storeMenuList.mapNotNull { rawData ->
                val menu = rawData as? Map<*, *> ?: return@mapNotNull null

                val optionList = (menu["optionList"] as? List<*>)?.mapNotNull { rawOption ->
                    val option = rawOption as? Map<*, *> ?: return@mapNotNull null
                    mapOf(
                        "optionName" to option["optionName"],
                        "optionPrice" to option["optionPrice"]
                    )
                } ?: emptyList()

                val totalOptionPrice = optionList.sumOf { it["optionPrice"] as? Int ?: 0 }

                mapOf(
                    "menu" to menu["menu"],
                    "amount" to menu["amount"],
                    "price" to ((menu["menuPrice"] as? Int) ?: 0) + totalOptionPrice,
                    "option" to optionList.map { it["optionName"] }
                )
            }
            val storeDocRef = db.collection("store_order").document(storeName)

            // 각 가게에 id=orderNum으로 menuList 저장
            val dataByStore = mapOf(orderNum to menuList)
            batch.set(storeDocRef, dataByStore, SetOptions.merge())
        }

        batch.commit()
            .addOnSuccessListener {
                Log.d("Firestore", "가게별 데이터 저장 완료")
            }
            .addOnFailureListener {
                Log.w("Firestore", "가게별 데이터 저장 실패 ", it)
            }

    }
}