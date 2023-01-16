package com.example.yuvish.models.DebtorsAPI.Market

data class MarketPaginationItem(
    val ber_date: String,
    val created_at: String,
    val date: String,
    val filial_id: Int,
    val id: Int,
    val nasiya: Int,
    val nasiyachi: Nasiyachi,
    val nasiyachi_id: Int,
    val order_id: Int,
    val status: String,
    val summa: Int,
    val updated_at: String,
    val user: User,
    val user_id: Int
)