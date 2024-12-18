package com.example.fitmeal

data class CartItem(
    val id: Int = 0,
    val cart_id: Int = 0, // Foreign key to Cart
    val item_id: Int = 0, // Foreign key to Item
    val quantity: Int = 0
)