package com.example.yuvish.Models.BaseIndikatorsIndex

data class SearchIndicatorsResult(
    val bekor_qilingan_buyurtmalar: Int,
    val berilgan_maoshlar_sum: Int,
    val joyida_yuvildi: JoyidaYuvildi,
    val kechilganlar: Int,
    val kunlik_maosh: Int,
    val mahsulot_qadoqlandi: MahsulotQadoqlandi,
    val mahsulot_topshirildi: MahsulotTopshirildi,
    val mahsulot_yuvildi: MahsulotYuvildi,
    val mijozlar: Int,
    val olib_kelinganlar: Int,
    val olingan_mahsulotlar: Int,
    val qabul_qilingan_buyurtmalar: Int,
    val qarzlar: Int,
    val qayta_qongirok_6_oylik: Int,
    val qayta_qongirok_kecha: Int,
    val qayta_qongiroklar: Int,
    val qayta_yuvishga_olindi: QaytaYuvishgaOlindi,
    val today_davomat: TodayAttendance?
)