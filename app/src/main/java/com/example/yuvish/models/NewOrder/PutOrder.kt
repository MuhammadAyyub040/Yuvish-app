package com.example.yuvish.models.NewOrder

data class PutOrder(
    val broken: String,
    val dirty: String,
    val giving_date: String,
    val izoh: String,
    val self_taking: Int,
    val items: List<PutService>
)