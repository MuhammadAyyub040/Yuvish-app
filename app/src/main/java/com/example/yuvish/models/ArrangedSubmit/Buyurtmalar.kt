package com.example.yuvish.models.ArrangedSubmit

data class Buyurtmalar(
    val count: Int,
    val hajm: Double,
    val id: Int,
    val name: String,
    val olchov: String,
    val products: List<Product>,
    val summa: Double
)