package com.example.yuvish.models.baseIndikatorsIndex

data class SubmittedIndicator(
    val current_page: Int,
    val `data`: List<SubmittedIndicatorOrder>,
    val from: Int,
    val last_page: Int,
    val path: String,
    val per_page: Int,
    val to: Int,
    val total: Int
)