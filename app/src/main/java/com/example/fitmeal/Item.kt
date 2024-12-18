package com.example.fitmeal

data class Item(
    val item_id: Int = 0,
    val name: String = "",
    val product_detail: String = "",
    val price: Double = 0.0,
    val quantity: Int = 0,
    val categories: String = "", // Use enum or constants for categories
    val likes: Int = 0
)