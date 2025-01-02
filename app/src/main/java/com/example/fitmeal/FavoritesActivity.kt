package com.example.fitmeal

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FavoritesActivity : AppCompatActivity() {

    private lateinit var favoritesRecyclerView: RecyclerView
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        favoritesRecyclerView = findViewById(R.id.rv_favorites)
        favoritesRecyclerView.layoutManager = LinearLayoutManager(this)

        loadFavoriteItems()
    }

    private fun loadFavoriteItems() {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            db.collection("favorites")
                .whereEqualTo("user_id", userId)
                .get()
                .addOnSuccessListener { documents ->
                    val favoriteItems = documents.toObjects(Favorite::class.java)
                    if (favoriteItems.isNotEmpty()) {
                        fetchItemDetails(favoriteItems)
                    } else {
                        Toast.makeText(this, "No favorite items found", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Failed to load favorite items: ${e.message}", Toast.LENGTH_LONG).show()
                }
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchItemDetails(favoriteItems: List<Favorite>) {
        val items = mutableListOf<Item>()
        for (favorite in favoriteItems) {
            db.collection("products")
                .document(favorite.item_id.toString())
                .get()
                .addOnSuccessListener { document ->
                    val item = document.toObject(Item::class.java)
                    if (item != null) {
                        items.add(item)
                        if (items.size == favoriteItems.size) {
                            val adapter = FavoriteItemAdapter(this, items)
                            favoritesRecyclerView.adapter = adapter
                        }
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Failed to load item details: ${e.message}", Toast.LENGTH_LONG).show()
                }
        }
    }
}