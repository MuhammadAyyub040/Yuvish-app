package com.example.yuvish.models.Warehouse

data class BasePagingModel <T> (
    val current_page: Int,
    val data: List<T>,
    val from: Int,
    val last_page: Int,
    val first_page_url: String,
    val last_page_url: String,
    val next_page_url: String,
    val prev_page_url: String,
    val path: String,
    val per_page: Int,
    val to: Int,
    val total: Int
)