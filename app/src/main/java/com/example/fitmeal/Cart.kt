package com.example.fitmeal

data class Cart(
    val cart_id: Int = 0,
    val user_id: Int = 0, // Foreign key to User
    val created_at: String = "" // Use appropriate date type
)