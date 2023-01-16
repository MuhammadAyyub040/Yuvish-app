package com.example.yuvish.models.DebtorsAPI.Market

data class Debt(
    val ber_date: String,
    val created_at: String,
    val date: String,
    val filial_id: Int,
    val id: Int,
    val nasiya: Int,
    val nasiyachi: Costumer,
    val nasiyachi_id: Int,
    val order_id: Int,
    val status: String,
    val summa: Int,
    val updated_at: String,
    val user_id: Int
)