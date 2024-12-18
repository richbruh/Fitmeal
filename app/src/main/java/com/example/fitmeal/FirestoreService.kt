package com.example.fitmeal

import com.google.firebase.firestore.FirebaseFirestore

class FirestoreService {

    private val db = FirebaseFirestore.getInstance()

    fun addUser(user: User, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val userRef = db.collection("users").document(user.user_id.toString())
        userRef.set(user)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onFailure(e) }
    }

    fun addItem(item: Item, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val itemRef = db.collection("items").document(item.itemID.toString())
        itemRef.set(item)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onFailure(e) }
    }

    fun addCart(cart: Cart, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val cartRef = db.collection("carts").document(cart.cart_id.toString())
        cartRef.set(cart)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onFailure(e) }
    }

    fun addCartItem(cartItem: CartItem, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val cartItemRef = db.collection("cart_items").document(cartItem.itemID.toString()) // change from item to itemID (19 Desember 2024) richbruh
        cartItemRef.set(cartItem)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onFailure(e) }
    }

    fun addFavorite(favorite: Favorite, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val favoriteRef = db.collection("favorites").document(favorite.id.toString())
        favoriteRef.set(favorite)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onFailure(e) }
    }
}