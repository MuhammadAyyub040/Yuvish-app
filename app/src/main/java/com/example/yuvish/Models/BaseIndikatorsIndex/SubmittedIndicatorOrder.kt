package com.example.yuvish.Models.BaseIndikatorsIndex

data class SubmittedIndicatorOrder(
    val costumer_id: Int,
    val costumer_name: String,
    val hisoblangan_summa: Int,
    val hodimlar: List<String>,
    val kechildi: Int,
    val order_nomer: Int,
    val products: List<IndicatorProduct>,
    val qarz: Int,
    val tolovlar: List<IndicatorPaymentType>,
    val tovar_dona: Int
)