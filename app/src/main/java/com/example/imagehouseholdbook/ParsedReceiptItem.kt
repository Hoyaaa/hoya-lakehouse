package com.example.imagehouseholdbook

data class ParsedReceiptItem(
    val productName: String,
    val unitPrice: Int,
    val receivedQty: Int,
    val totalAmount: Int
)
