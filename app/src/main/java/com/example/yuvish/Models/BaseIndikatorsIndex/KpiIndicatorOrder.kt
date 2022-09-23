package com.example.yuvish.Models.BaseIndikatorsIndex

data class KpiIndicatorOrder(
    val filial_id: Int,
    val id: Int,
    val keldi: Int,
    val keldi_time: String,
    val ketdi: Int,
    val ketdi_time: String,
    val maosh: Int,
    val sana: String,
    val status: Int,
    val type: Int,
    val user: KpiIndicatorUser,
    val user_id: Int
)