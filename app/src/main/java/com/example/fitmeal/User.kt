package com.example.fitmeal

data class User(
    val user_id: String = "",
    val username: String = "",
    val phone_number: String = "",
    val email: String = "",
    val role: String = "" // Use enum or constants for 'admin' and 'user'
)