package com.example.fitmeal


data class CartItem(
    val name: String,
    val price: Int,
    var quantity: Int,
    val imageResId: Int
)
