package com.example.yuvish.Models.DebtorsPackage

data class Debtors(
    val ber_date: String,
    val costumer: Costumer,
    val date: String,
    val filial_id: Int,
    val id: Int,
    val nasiya: Int,
    val nasiyachi_id: Int,
    val order_id: Int,
    val status: String,
    val summa: Int,
    val user: User,
    val user_id: Int
)