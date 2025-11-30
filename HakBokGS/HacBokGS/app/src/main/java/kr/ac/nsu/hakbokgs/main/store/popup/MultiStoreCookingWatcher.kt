package kr.ac.nsu.hakbokgs.main.store.popup

import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kr.ac.nsu.hakbokgs.main.common.CurrentActivityProvider

object MultiStoreCookingWatcher {
    private val listenerRegistrations = mutableMapOf<String, ListenerRegistration>()
    private val previousCompleteMap = mutableMapOf<String, Boolean>()
    private val shownPopupStores = mutableSetOf<String>()
    private val pendingPopups = mutableListOf<Triple<String, String, String>>() // store, menu, orderNumber
    private val orderNumberToDocIdMap = mutableMapOf<String, String>() // 🧭 역매핑

    // 감지 중지
    fun stopWatching() {
        listenerRegistrations.values.forEach { it.remove() }
        listenerRegistrations.clear()
        previousCompleteMap.clear()
        shownPopupStores.clear()
        pendingPopups.clear()
        orderNumberToDocIdMap.clear()
        Log.d("CookingWatcher", "👋 전체 감지 중단됨")
    }

    // 감지 시작
    fun startWatching(userId: String, orderDocId: String) {
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("users").document(userId)
            .collection("orders").document(orderDocId)

        val registration = docRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.e("CookingWatcher", "❌ Listen 실패", error)
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                val orderNumber = snapshot.getLong("orderNumber")?.toString() ?: return@addSnapshotListener
                val snapshotData = snapshot.data ?: return@addSnapshotListener

                orderNumberToDocIdMap[orderNumber] = orderDocId // ✅ 매핑 저장

                for ((storeName, value) in snapshotData) {
                    if (value is Map<*, *>) {
                        val completeNow = value["complete"] as? Boolean ?: false
                        val completeBefore = previousCompleteMap["$orderDocId:$storeName"] ?: false
                        val popupAlreadyShown = value["popupShown"] as? Boolean ?: false

                        Log.d("CookingWatcher", "📍 [$storeName] 조건 검사 중")
                        Log.d("CookingWatcher", "   - completeNow: $completeNow")
                        Log.d("CookingWatcher", "   - completeBefore: $completeBefore")
                        Log.d("CookingWatcher", "   - popupAlreadyShown: $popupAlreadyShown")

                        if (!completeBefore && completeNow && !popupAlreadyShown) {
                            previousCompleteMap["$orderDocId:$storeName"] = true
                            shownPopupStores.add("$orderDocId:$storeName")

                            val menuList = value["menuList"] as? List<Map<String, Any>>
                            val menuName = menuList?.firstOrNull()?.get("menu") as? String ?: "메뉴"

                            val currentActivity: Activity? = CurrentActivityProvider.getCurrentActivity()
                            Log.d("CookingWatcher", "🎯 현재 액티비티: $currentActivity")

                            if (currentActivity != null && !currentActivity.isFinishing) {
                                showPopupNow(currentActivity, storeName, menuName, orderNumber)
                                docRef.update("$storeName.popupShown", true)
                                    .addOnSuccessListener {
                                        Log.d("CookingWatcher", "📌 Firestore 업데이트 완료 → $storeName.popupShown = true")
                                    }
                                    .addOnFailureListener {
                                        Log.e("CookingWatcher", "❌ Firestore 업데이트 실패", it)
                                    }
                            } else {
                                Log.w("CookingWatcher", "⚠️ UI 미준비 상태에서 팝업 표시 시도 → 재시도 예정")
                                pendingPopups.add(Triple(storeName, menuName, orderNumber))
                                schedulePopupRetry()
                            }
                        }
                    }
                }
            }
        }

        listenerRegistrations[orderDocId] = registration
        Log.d("CookingWatcher", "👀 감지 시작됨 → userId=$userId, orderDocId=$orderDocId")
    }

    // 보류된 팝업 표시
    fun showPendingPopups(activity: Activity) {
        if (pendingPopups.isEmpty()) {
            Log.d("CookingWatcher", "📭 보류된 팝업 없음")
            return
        }

        val iterator = pendingPopups.iterator()
        while (iterator.hasNext()) {
            val (store, menu, orderNumber) = iterator.next()
            val orderDocId = orderNumberToDocIdMap[orderNumber]
            if (orderDocId == null) {
                Log.e("CookingWatcher", "❓ orderDocId 매핑 실패 → orderNumber: $orderNumber")
                continue
            }

            if (!shownPopupStores.contains("$orderDocId:$store")) {
                showPopupNow(activity, store, menu, orderNumber)

                val db = FirebaseFirestore.getInstance()
                val docRef = db.collection("users").document(FirebaseAuth.getInstance().currentUser?.email ?: return)
                    .collection("orders").document(orderDocId)
                docRef.update("$store.popupShown", true)

                iterator.remove()
            }
        }
    }

    // 팝업 표시 함수
    private fun showPopupNow(context: Activity, store: String, menu: String, orderNumber: String) {
        CookingStatePopup.showCookingDonePopup(
            context = context,
            storeName = store,
            menuName = menu,
            orderNumber = orderNumber
        )
        Log.d("CookingWatcher", "✅ 팝업 표시: $store - $menu")
    }

    // 1초 간격으로 UI 준비될 때까지 재시도
    private fun schedulePopupRetry() {
        Handler(Looper.getMainLooper()).postDelayed({
            val currentActivity = CurrentActivityProvider.getCurrentActivity()
            if (currentActivity != null && !currentActivity.isFinishing) {
                showPendingPopups(currentActivity)
            } else {
                Log.w("CookingWatcher", "⚠️ UI 미준비 상태에서 팝업 표시 시도 → 재시도 예정")
                schedulePopupRetry()
            }
        }, 1000)
    }
    fun startWatchingAllStores(userId: String) {
        stopWatching()

        val db = FirebaseFirestore.getInstance()
        db.collection("users").document(userId)
            .collection("orders")
            .get()
            .addOnSuccessListener { documents ->
                for (doc in documents) {
                    val orderDocId = doc.id
                    startWatching(userId, orderDocId)
                }
                Log.d("CookingWatcher", "🚀 전체 주문 감지 등록 완료 (${documents.size()}건)")
            }
            .addOnFailureListener { e ->
                Log.e("CookingWatcher", "🔥 전체 주문 감지 실패", e)
            }
    }

}
