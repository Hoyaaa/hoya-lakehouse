package com.example.imagehouseholdbook

// 6컬럼(가맹점 명, 구매 날짜, 상품명, 단가, 수령, 금액)만 사용하는 모델
data class HouseholdData(
    val docId: String = "",
    val date: String = "",
    val storeName: String = "",      // 가맹점 명
    val productName: String = "",    // 상품명
    val unitPrice: Long = 0L,        // 단가
    val receivedQty: Long = 0L,      // 수령
    val totalAmount: Long = 0L       // 금액 (unitPrice * receivedQty)
)

// Main 리스트에서 “묶음(그룹)”으로 보여주기 위한 UI용 모델
// (place 필드는 기존 어댑터/레이아웃 재사용을 위해 이름 유지, 실제 값은 storeName을 넣어 사용)
data class HouseholdGroup(
    val date: String,
    val place: String,
    val totalPrice: Long,            // 그룹 총 금액 합산
    val representativeName: String,
    val itemDocIds: ArrayList<String> = arrayListOf()
)
