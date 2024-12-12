package com.example.fitmeal

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore

class HomeActivity : AppCompatActivity() {

    private lateinit var popularNowRecyclerView: RecyclerView
    private lateinit var exclusiveOfferingRecyclerView: RecyclerView
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)

        // Initialize BottomNavigationView
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav.selectedItemId = R.id.navigation_shop

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_shop -> {
                    startActivity(Intent(this, HomeActivity::class.java))
                    true
                }
                R.id.navigation_favorite -> {
                    startActivity(Intent(this, FavoritesActivity::class.java))
                    true
                }
                R.id.navigation_account -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    true
                }
                R.id.navigation_cart -> {
                    startActivity(Intent(this, MyCartActivity::class.java))
                    true
                }
                else -> false
            }
        }

        // Initialize RecyclerViews
        popularNowRecyclerView = findViewById(R.id.rv_popular_now)
        exclusiveOfferingRecyclerView = findViewById(R.id.rv_exclusive_offering)

        // Set layout managers
        popularNowRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        exclusiveOfferingRecyclerView.layoutManager = GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false)

        // Load products from Firestore
        loadPopularNowItems()
        loadExclusiveOfferingItems()
    }

    private fun loadPopularNowItems() {
        db.collection("products")
            .orderBy("price", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .limit(10)
            .get()
            .addOnSuccessListener { documents ->
                val popularItems = documents.toObjects(Item::class.java)
                if (popularItems.isNotEmpty()) {
                    val adapter = ItemAdapter(popularItems, R.id.fragment_container)
                    popularNowRecyclerView.adapter = adapter
                } else {
                    Toast.makeText(this, "No popular items found", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to load popular items: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }

    private fun loadExclusiveOfferingItems() {
        db.collection("products")
            .get()
            .addOnSuccessListener { documents ->
                val exclusiveItems = documents.toObjects(Item::class.java)
                if (exclusiveItems.isNotEmpty()) {
                    val adapter = ItemAdapter(exclusiveItems, R.id.fragment_container)
                    exclusiveOfferingRecyclerView.adapter = adapter
                } else {
                    Toast.makeText(this, "No exclusive offerings found", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to load exclusive offerings: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }
}