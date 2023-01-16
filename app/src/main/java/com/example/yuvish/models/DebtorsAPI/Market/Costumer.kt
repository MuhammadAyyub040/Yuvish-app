package com.example.yuvish.models.DebtorsAPI.Market

data class Costumer(
    val costumer_addres: String,
    val costumer_date: String,
    val costumer_name: String,
    val costumer_phone_1: String,
    val costumer_phone_2: String,
    val costumer_source: String,
    val costumer_status: String,
    val costumer_turi: String,
    val costumers_filial_id: Int,
    val id: Int,
//    @ColumnInfo(name = "costumer_comment")
    val izoh: String,
    val manba: String,
    val millat_id: Int,
//    @ColumnInfo(name = "costumer_region_id")
    val mintaqa_id: Int,
    val parol: String,
//    @ColumnInfo(name = "costumer_saygak_id")
    val saygak_id: Int,
    val token: String,
    val user_id: Int
)