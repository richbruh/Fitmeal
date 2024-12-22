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
        val userId = auth.currentUser?.uid?.toIntOrNull()
        if (userId != null) {
            db.collection("favorites")
                .whereEqualTo("user_id", userId)
                .get()
                .addOnSuccessListener { documents ->
                    val favoriteItems = documents.toObjects(Favorite::class.java)
                    if (favoriteItems.isNotEmpty()) {
                        val adapter = FavoriteItemAdapter(this, favoriteItems)
                        favoritesRecyclerView.adapter = adapter
                    } else {
                        Toast.makeText(this, "No favorite items found", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Failed to load favorite items: ${e.message}", Toast.LENGTH_LONG).show()
                }
        } else {
            Toast.makeText(this, "User not logged in or invalid user ID", Toast.LENGTH_SHORT).show()
        }
    }
}