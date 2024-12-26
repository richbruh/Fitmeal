package com.example.fitmeal

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

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

    fun addOrUpdateCart(cart: Cart, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val cartRef = db.collection("carts").document(cart.cartID)
        cartRef.set(cart, SetOptions.merge())
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onFailure(e) }
    }

    fun getCartByUserId(
        userId: String,
        onSuccess: (Cart?) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        db.collection("carts")
            .whereEqualTo("user_id", userId)
            .get()
            .addOnSuccessListener { snapshot ->
                val cart = snapshot.documents.firstOrNull()?.toObject(Cart::class.java)
                onSuccess(cart)
            }
            .addOnFailureListener { e -> onFailure(e) }
    }

    fun addFavorite(favorite: Favorite, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val favoriteRef = db.collection("favorites").document(favorite.id.toString())
        favoriteRef.set(favorite)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onFailure(e) }
    }
}