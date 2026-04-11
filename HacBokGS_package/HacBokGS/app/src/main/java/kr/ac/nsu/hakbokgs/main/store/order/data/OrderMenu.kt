package kr.ac.nsu.hakbokgs.main.store.order.data

import com.google.firebase.Timestamp

data class OrderData(
    val orderNum: Int? = 0,
    val payment: String? = "",
    val orderDate: Timestamp? = null,
    val storeMap: MutableMap<String, StoreOrder>
)

data class StoreOrder(
    val complete: Boolean = false,
    val menuList: List<OrderMenu>? = listOf()
)

data class OrderMenu(
    val menu: String = "",
    val menuPrice: Int = 0,
    val amount: Int = 1,
    val optionList: List<Option>? = null
)

data class Option(
    val optionName: String = "",
    val optionPrice: Int = 0
)