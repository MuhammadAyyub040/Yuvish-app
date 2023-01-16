package com.example.yuvish.models.ReadyOrders

data class ViewOrderItem(
    val barcode: String,
    val clean_date: String,
    val clean_filial_id: String,
    val clean_hajm: Int,
    val clean_narx: Int,
    val clean_product: Int,
    val clean_status: String,
    val costumer_id: Int,
    val gilam_boyi: Int,
    val gilam_eni: Int,
    val id: Int,
    val joy: String,
    val joyida_date: String,
    val narx: Int,
    val order_id: Int,
    val qad_date: String,
    val qad_user: Int,
    val qayta_sana: String,
    val reclean_driver: Int,
    val reclean_place: Int,
    val sana: String,
    val top_sana: String,
    val top_user: Int,
    val user_id: Int,
    val xizmat: Xizmat
)