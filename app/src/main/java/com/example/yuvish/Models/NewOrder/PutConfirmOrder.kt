package com.example.yuvish.Models.NewOrder

data class PutConfirmOrder(
    val izoh: String,
    val items: List<PutService>
)