package com.example.fitmeal

data class Favorite(
    val id: Int = 0,
    val user_id: Int = 0, // Foreign key to User
    val item_id: Int = 0 // Foreign key to Item
)