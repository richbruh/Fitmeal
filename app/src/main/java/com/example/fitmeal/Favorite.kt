package com.example.fitmeal

data class Favorite(
    val id: Int = 0,
    val user_id: String = "", // Foreign key to User
    val item_id: Int = 0, // Foreign key to Item
    val name: String = "",
    val price: Int = 0,
    val imageUrl: String = ""
)

/*
data class Favorite(
    val user_id: String = "",
    val item_id: String = "",
    val name: String = "",
    val price: Double = 0.0,
    val imageUrl: String = ""
)

 */