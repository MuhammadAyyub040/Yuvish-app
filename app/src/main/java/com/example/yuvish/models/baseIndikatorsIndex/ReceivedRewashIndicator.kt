package com.example.yuvish.models.baseIndikatorsIndex

data class ReceivedRewashIndicator(
    val data: List<ReceivedRewashIndicatorOrder>,
    val jami_table_footer: List<IndicatorProduct>
)