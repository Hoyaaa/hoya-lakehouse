package kr.ac.nsu.hakbokgs.main.store.popup

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kr.ac.nsu.hakbokgs.main.store.popup.MultiStoreCookingWatcher

object CookingStateRecovery {

    fun recoverWatching(context: Context) {
        val user = FirebaseAuth.getInstance().currentUser ?: return
        val userId = user.email ?: return
        val db = FirebaseFirestore.getInstance()

        db.collection("users").document(userId)
            .collection("orders")
            .get()
            .addOnSuccessListener { snapshot ->
                if (snapshot.isEmpty) return@addOnSuccessListener

                for (document in snapshot.documents) {
                    val orderDocId = document.id
                    MultiStoreCookingWatcher.startWatching(userId, orderDocId)
                }
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
            }
    }

}
