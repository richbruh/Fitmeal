package com.example.fitmeal

data class AdminStock(
    var name: String,
    var details: String,
    var price: Double,
    val imageRes: Int,
    var productQuantity: Int = 0 // Default quantity set to 0 (or any initial value)
)


