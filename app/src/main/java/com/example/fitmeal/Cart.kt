package com.example.fitmeal

data class Cart(
    val cartID: String = "",
    val user_id: String = "", // String untuk mencocokkan dengan userId
    val createdAt: Long = System.currentTimeMillis(), // Timestamp
    val items: MutableList<CartItem> = mutableListOf()
)