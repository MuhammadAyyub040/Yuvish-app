package com.example.yuvish.Models.NewOrder

data class ConfirmProduct(
    val filial_id: Int,
    val id: Int,
    val order_id: Int,
    val status: Int,
    var value: Int,
    val x_id: Int,
    val xizmat: Xizmat
)