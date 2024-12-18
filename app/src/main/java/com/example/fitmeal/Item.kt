package com.example.fitmeal

data class Item(
    val itemID: Int,
    var name: String,
    var details: String,
    var price: Int,
    var stock: Int,
    var imageUrl: String
)
