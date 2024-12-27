package com.example.fitmeal

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class FavoritesActivity : AppCompatActivity() {

    private lateinit var backBtn: ImageView
    private lateinit var addAllToCartBtn: Button
    private lateinit var favoritesListView: ListView
    private lateinit var adapter: FavoriteItemAdapter
    private val firestoreService = FirestoreService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        // Inisialisasi BottomNavigationView
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav.selectedItemId = R.id.navigation_favorite // Highlight Favorite
        BottomNavHelper.setupBottomNav(bottomNav, this)

        // Inisialisasi views
        backBtn = findViewById(R.id.btnBack)
        addAllToCartBtn = findViewById(R.id.btnAddAllToCart)
        favoritesListView = findViewById(R.id.favoritesList)

        // Data favorit dari Firestore
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            fetchFavoriteItems(user.uid)
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
        }

        // Tombol Back action
        backBtn.setOnClickListener {
            // Pindah ke HomeActivity ketika back button ditekan
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish() // Tutup FavoritesActivity
        }

        // Logika ketika Add All to Cart ditekan
        addAllToCartBtn.setOnClickListener {
            // Logika untuk menambah semua item ke keranjang
            Toast.makeText(this, "All items added to cart", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        // Arahkan ke HomeActivity ketika tombol back fisik ditekan
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish() // Tutup FavoritesActivity
    }

    private fun fetchFavoriteItems(userId: String) {
        firestoreService.getFavoriteItems(userId,
            onSuccess = { favoriteItemIds ->
                // Ambil detail item dari Firestore berdasarkan ID favorit
                fetchItemsDetails(favoriteItemIds)
            },
            onFailure = { exception ->
                Toast.makeText(
                    this,
                    "Failed to load favorites: ${exception.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        )
    }

    private fun fetchItemsDetails(favoriteItemIds: List<String>) {
        val favoriteItems = mutableListOf<FavoriteItem>()
        if (favoriteItemIds.isNotEmpty()) {
            for (itemId in favoriteItemIds) {
                firestoreService.getItemById(itemId,
                    onSuccess = { item ->
                        if (item != null) {
                            favoriteItems.add(
                                FavoriteItem(
                                    name = item.name,
                                    quantity = "${item.stock} pcs",
                                    price = item.price,
                                    imageResId = R.drawable.fruit // Default placeholder
                                )
                            )
                            // Update ListView ketika semua item selesai dimuat
                            if (favoriteItems.size == favoriteItemIds.size) {
                                adapter = FavoriteItemAdapter(this, favoriteItems)
                                favoritesListView.adapter = adapter
                            }
                        }
                    },
                    onFailure = { exception ->
                        Toast.makeText(
                            this,
                            "Failed to load item: ${exception.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                )
            }
        } else {
            // Tidak ada item favorit
            Toast.makeText(this, "No favorite items found", Toast.LENGTH_SHORT).show()
        }
    }
}