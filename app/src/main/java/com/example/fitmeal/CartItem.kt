package com.example.fitmeal

data class CartItem(
    val cartID: Int,
    val itemID: Int,
    var quantity: Int
)

/*
data class CartItem(
    val id: Int = 0,
    val cartID: Int = 0, // Foreign key to Cart
    val itemID: Int = 0, // Foreign key to Item
    val quantity: Int = 0
)*/