package com.example.yuvish.models.baseIndikatorsIndex

data class Data(
    val costumer_id: Int,
    val costumer_name: String,
    val hodimlar: List<String>,
    val order_nomer: Int,
    val products: List<IndicatorProduct>,
    val tovar_dona: Int
)