package kr.ac.nsu.hakbokgs.main.store.cart.data

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Transaction

object OrderNumManage {
    fun getOrderNum(listener: OnOrderNumListener) {
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("store_order").document("orderNum")

        db.runTransaction(Transaction.Function { transaction ->
            val snapshot = transaction[docRef]
            val currentNum = snapshot.getLong("number")
            val newNum = (currentNum?.toInt() ?: 0) + 1

            transaction.update(docRef, "number", newNum) // 다음 숫자 넣기
            newNum
        }).addOnSuccessListener { newNum ->
            listener.onSuccess(newNum)
        }.addOnFailureListener { e: Exception? ->
            Log.e("Firestore", "주문번호 가져오기 실패")
            listener.onFailure(e)
        }
    }

    interface OnOrderNumListener {
        fun onSuccess(newNum: Int)

        fun onFailure(e: Exception?)
    }
}