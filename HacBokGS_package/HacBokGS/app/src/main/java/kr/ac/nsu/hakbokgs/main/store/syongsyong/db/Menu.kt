package kr.ac.nsu.hakbokgs.main.store.syongsyong.db

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Menu(
    var id: String? = null, // 메뉴이름
    var description: String? = null,
    var imagePath: String? = null,
    var ingredient: List<String>? = null,
    var SalesStatus: String? = null,
    var size: Size? = null,
    var documentId: String = ""
) : Parcelable

@Parcelize
data class Size(
    var basic: BasicSize? = null
) : Parcelable

@Parcelize
data class BasicSize(
    var price: String? = null
) : Parcelable