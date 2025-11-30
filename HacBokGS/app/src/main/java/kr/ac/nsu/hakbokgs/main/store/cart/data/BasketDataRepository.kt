package kr.ac.nsu.hakbokgs.main.store.cart.data

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore

object BasketDataRepository {
    val tag: String = "BasketDataResource"

    /* 데이터 가져오기 */
    fun getBasketMenu(userId: String, onComplete: (List<BasketMenu>) -> Unit) {
        val db = FirebaseFirestore.getInstance()

        db.collection("users")  // ✅ 경로 수정
            .document(userId).collection("basket")
            .get()
            .addOnSuccessListener { result ->
                val basketList = result.documents.mapNotNull { doc ->
                    Log.d("Firestore", doc.data.toString())
                    try {
                        val basket = doc.toObject(BasketMenu::class.java)
                        basket?.copy(id = doc.id)
                        basket
                    } catch (e: Exception) {
                        Log.w(tag, "장바구니 데이터 변환 실패: ${doc.id}:${doc.data}")
                        null
                    }
                }
                Log.i("Firestore", "$userId : 장바구니 데이터 로드 성공 ${basketList.size}개")
                onComplete(basketList)
            }
            .addOnFailureListener { e ->
                Log.w("Firestore", "장바구니 데이터 가져오기 실패: $e")
                onComplete(emptyList())
            }
    }


    /* 데이터 삭제하기 */
    fun deleteBasketMenu(userId: String, id: String, onComplete: () -> Unit) {
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("users") // ✅ 변경됨
            .document(userId).collection("basket")
            .document(id)

        docRef.delete()
            .addOnSuccessListener {
                onComplete()
            }
            .addOnFailureListener { e ->
                Log.e(tag, "삭제 실패", e)
            }
    }

    fun updateBasketMenu(userId: String, updateMenu: BasketMenu, onComplete: () -> Unit) {
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("users") // ✅ 변경됨
            .document(userId).collection("basket")
            .document(updateMenu.id)

        docRef.set(updateMenu)
            .addOnSuccessListener { onComplete() }
            .addOnFailureListener { e -> Log.e(tag, "업데이트 실패", e) }
    }

}