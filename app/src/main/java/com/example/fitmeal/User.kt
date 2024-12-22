package com.example.fitmeal

data class User(
    val user_id: Int = 0,
    var username: String = "",
    var phone_number: String = "",
    var email: String = "",
    var role: String = "" // Use enum or constants for 'admin' and 'user'
)