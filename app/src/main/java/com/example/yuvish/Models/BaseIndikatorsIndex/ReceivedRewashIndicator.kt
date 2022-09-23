package com.example.yuvish.Models.BaseIndikatorsIndex

data class ReceivedRewashIndicator(
    val data: List<ReceivedRewashIndicatorOrder>,
    val jami_table_footer: List<IndicatorProduct>
)