package com.example.imagehouseholdbook

data class OcrResponse(
    val images: List<OcrImageResult>
)

data class OcrImageResult(
    val fields: List<OcrField>
)

// [수정] Any? -> OcrBoundingPoly로 변경하여 좌표 사용 가능하게 함
data class OcrField(
    val inferText: String,
    val boundingPoly: OcrBoundingPoly
)

data class OcrBoundingPoly(
    val vertices: List<OcrVertex>
)

data class OcrVertex(
    val x: Double,
    val y: Double
)
