package kr.ac.nsu.hakbokgs.main.store.taesan.db


import android.os.Parcelable
import androidx.annotation.Keep

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

@Keep
@Parcelize
data class Size(
    var baekban: SizeOption? = null,
    var jeongsik: SizeOption? = null,
    var basic: SizeOption? = null,
    ) : Parcelable

@Keep
@Parcelize
data class SizeOption(
    var price: String? = null
) : Parcelable
