package com.example.yuvish.models

data class CommonSettings(
    val adding_nation: Int, //AddCustomer da so'zlashuv tili ko'rinishi
    val barcode: Int, //asosiy oyna barcode va barcha sariq printlar
    val changing_coast: Int, //getSize da narx o'zgarishi yoki o'zgarmasligi
    val order_brak: Int, //writeReceipt da brak tanlash ko'rinishi
    val order_dog: Int, //writeReceipt da dog' tanlash ko'rinishi
    val sizing: Int, // !kerak emas!
    val transfer_driver: Int, // !kerak emas!
    val tokcha: Boolean, // Tokcha bo'limi ko'rinishi va qadoqlashda tokcha tanlash dialog ko'rinishi ***
    val majburiy_chegirma: Int // !kerak emas!
)