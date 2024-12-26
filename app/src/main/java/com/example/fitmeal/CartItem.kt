package com.example.fitmeal

data class CartItem(
    val itemID: Int = 0, // Foreign key to Item,
    var quantity: Int = 1
)