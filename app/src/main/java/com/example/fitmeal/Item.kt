package com.example.fitmeal

data class Item(
    val itemID: Int = 0,
    val name: String = "",
    val details: String = "",
    val price: Int = 0,
    val stock: Int = 0,
    var imageUrl: String = ""
) {
    // No-argument constructor for Firestore
    constructor() : this(0, "", "", 0, 0, "")
}