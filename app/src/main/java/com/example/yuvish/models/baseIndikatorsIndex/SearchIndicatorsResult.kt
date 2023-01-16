package com.example.yuvish.models.baseIndikatorsIndex

data class SearchIndicatorsResult(
    val buyurtma_bekor_qilindi: Int,
    val buyurtma_qabul_qilindi: Int,
    val davomat: Davomat,
    val hajm: Double,
    val joyida_yuvildi: Double,
    val kechagi_call: Int,
    val kechim: Int,
    val kpi: Int,
    val maosh: Int,
    val maosh_history: Int,
    val mijozlar: Int,
    val nasiya: Int,
    val ozi_olib_ket: Int,
    val qadoqlandi: Qadoqlandi,
    val qayta: Qayta,
    val recall6: Int,
    val rejadagi_call: Int,
    val topshirildi: Topshirildi,
    val yuvildi: Yuvildi
)