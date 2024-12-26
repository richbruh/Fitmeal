package com.example.fitmeal

data class Item(
    val itemID: Int = 0,
    var name: String = "",
    var details: String = "",
    var price: Int = 0,
    var stock: Int = 0,
    var imageUrl: String = "",
    var category: CharCategory = CharCategory.UNCATEGORIZED
) {
    // No-argument constructor for Firestore
    constructor() : this(
        itemID = 0,
        name = "",
        details = "",
        price = 0,
        stock = 0,
        imageUrl = "",
        category = CharCategory.UNCATEGORIZED
    )
}
