package com.example.yuvish.retrofit

object ApiClient {
    object INITIAL_PAGE_NUMBER {
        const val PAGE_SIZE = 10
        const val INITIAL_PAGE_NUMBER = 1
    }

    const val BASE_URL = "https://back.yuvish.uz/"

    const val IMAGE_BASE_URL = "https://yuvish.uz/images/logo/"

    //Barcode
    const val BARCODE_IMAGE_WIDTH = 900
    const val BARCODE_IMAGE_HEIGHT = 300

    //Print
    const val BARCODE_PRINT_FILE_NAME = "Product barcode"
    const val RECEIPT_PRINT_FILE_NAME = "Order receipt"

    val retrofitService: RetrofitService by lazy {
        RetrofitClient.getRetrofit(BASE_URL).create(RetrofitService::class.java)
    }
}