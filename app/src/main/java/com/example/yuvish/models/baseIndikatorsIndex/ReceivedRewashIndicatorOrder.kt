package com.example.yuvish.models.baseIndikatorsIndex

data class ReceivedRewashIndicatorOrder(
    val costumer_id: Int,
    val costumer_name: String,
    val order_nomer: Int,
    val products: List<IndicatorProduct>,
    val tovar_dona: Int
)