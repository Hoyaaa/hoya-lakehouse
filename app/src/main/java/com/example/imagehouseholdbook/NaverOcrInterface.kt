package com.example.imagehouseholdbook

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface NaverOcrInterface {
    @Multipart
    @POST("https://wdanrrupqp.apigw.ntruss.com/custom/v1/49909/d8a87c1b123014e55e5a694ec1f40d4477200073fc704a595652a410346ec221/general")
    fun uploadImage(
        @Header("X-OCR-SECRET") secretKey: String,
        @Part file: MultipartBody.Part,
        @Part("message") message: RequestBody
    ): Call<OcrResponse>
}