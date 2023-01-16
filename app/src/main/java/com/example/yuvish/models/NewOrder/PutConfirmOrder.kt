package com.example.yuvish.models.NewOrder

data class PutConfirmOrder(
    val izoh: String,
    val items: List<PutService>
)