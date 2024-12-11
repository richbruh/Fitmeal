package com.example.fitmeal

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class FavoritesActivity : AppCompatActivity() {

    private lateinit var backBtn: ImageView
    private lateinit var addAllToCartBtn: Button
    private lateinit var favoritesListView: ListView
    private lateinit var adapter: FavoriteItemAdapter

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

        // Data favorit untuk contoh
        val favoriteItems = mutableListOf(
            FavoriteItem("Bell Pepper Red", "1pcs, Price", 22000, R.drawable.fruit),
            FavoriteItem("Egg Chicken Red", "1 Basket, Price", 90000, R.drawable.fruit),
            // Tambahkan item lainnya di sini
        )

        // Set adapter untuk ListView
        adapter = FavoriteItemAdapter(this, favoriteItems)
        favoritesListView.adapter = adapter

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
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        // Arahkan ke HomeActivity ketika tombol back fisik ditekan
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish() // Tutup FavoritesActivity
    }
}
