package com.example.yuvish.models.NewOrder

data class SearchCustomerResult(
    val active_orders: List<ActiveOrder>,
    val costumer1: Costumer?,
    val qabulqilinadigan_order_id: Int?,
    val tasdilanadigan_order_id: Int?
)