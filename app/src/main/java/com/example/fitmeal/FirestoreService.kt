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

    fun addFavoriteItem(
        userId: String,
        itemId: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val favorite = mapOf(
            "user_id" to userId,
            "item_id" to itemId
        )
        db.collection("favorites")
            .add(favorite)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onFailure(e) }
    }

    fun removeFavoriteItem(
        userId: String,
        itemId: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        db.collection("favorites")
            .whereEqualTo("user_id", userId)
            .whereEqualTo("item_id", itemId)
            .get()
            .addOnSuccessListener { snapshot ->
                for (document in snapshot.documents) {
                    db.collection("favorites").document(document.id).delete()
                        .addOnSuccessListener { onSuccess() }
                        .addOnFailureListener { e -> onFailure(e) }
                }
            }
            .addOnFailureListener { e -> onFailure(e) }
    }

    fun getFavoriteItems(
        userId: String,
        onSuccess: (List<String>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        db.collection("favorites")
            .whereEqualTo("user_id", userId)
            .get()
            .addOnSuccessListener { snapshot ->
                val favoriteItems = snapshot.documents.map { it["item_id"].toString() }
                onSuccess(favoriteItems)
            }
            .addOnFailureListener { e ->
                onFailure(e)
            }
    }

    fun getItemById(itemId: String, onSuccess: (Item?) -> Unit, onFailure: (Exception) -> Unit) {
        if (itemId.isNotEmpty()) {
            db.collection("items").document(itemId)
                .get()
                .addOnSuccessListener { document ->
                    val item = document.toObject(Item::class.java)
                    onSuccess(item)
                }
                .addOnFailureListener { exception ->
                    onFailure(exception)
                }
        } else {
            onFailure(IllegalArgumentException("Invalid item ID"))
        }
    }
}