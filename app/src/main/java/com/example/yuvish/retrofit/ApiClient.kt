package com.example.yuvish.retrofit

object ApiClient {
    object INITIAL_PAGE_NUMBER {
        const val PAGE_SIZE = 10
        const val INITIAL_PAGE_NUMBER = 1
    }

    const val BASE_URL = "https://back.yuvish.uz/"

    val retrofitService: RetrofitService by lazy {
        RetrofitClient.getRetrofit(BASE_URL).create(RetrofitService::class.java)
    }
}