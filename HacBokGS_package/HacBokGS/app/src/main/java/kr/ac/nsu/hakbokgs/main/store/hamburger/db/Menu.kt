package kr.ac.nsu.hakbokgs.main.store.hamburger.db;

import com.google.firebase.firestore.DocumentId

import java.io.Serializable

data class Menu(
    @DocumentId
    var documentId: String = "",
    val id: String = "",
    val SalesStatus: String? = null,
    val description: String? = null,
    val imagePath: String? = null,
    val ingredient: List<String>? = null,
    val size: Map<String, *>? = null
) : Serializable


data class Size(
    var basics: SizePrice? = null,
    var single: SizePrice? = null,
    var set: SizePrice? = null,
    var medium: SizePrice? = null,
    var small: SizePrice? = null
) : Serializable

data class SizePrice(
    var price: String? = ""
) : Serializable
