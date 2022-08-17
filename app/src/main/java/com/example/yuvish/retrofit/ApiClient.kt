package com.example.yuvish.retrofit

object ApiClient {
    const val BASE_URL = "https://back.yuvish.uz/"

    val retrofitService: RetrofitService by lazy {
        RetrofitClient.getRetrofit(BASE_URL).create(RetrofitService::class.java)
    }
}