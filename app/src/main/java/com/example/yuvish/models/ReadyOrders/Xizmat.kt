package com.example.yuvish.models.ReadyOrders

data class Xizmat(
    val discount_for_own: Int,
    val filial_id: Int,
    val min_narx: Int,
    val narx: Int,
    val olchov: String,
    val saygak_narx: Int,
    val status: String,
    val xizmat_id: Int,
    val xizmat_turi: String
)