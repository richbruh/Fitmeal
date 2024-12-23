package com.example.fitmeal

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class HomeActivity : AppCompatActivity() {

    private lateinit var popularNowRecyclerView: RecyclerView
    private lateinit var exclusiveOfferingRecyclerView: RecyclerView
    private lateinit var searchEditText: EditText
    private lateinit var popularNowText: TextView
    private lateinit var exclusiveOfferingText: TextView
    private val db = FirebaseFirestore.getInstance()
    private lateinit var popularNowAdapter: ItemAdapter
    private lateinit var exclusiveOfferingAdapter: ExclusiveOfferingAdapter
    private var allPopularItems: List<Item> = emptyList()
    private var allExclusiveItems: List<Item> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)

        // Initialize views
        popularNowRecyclerView = findViewById(R.id.rv_popular_now)
        exclusiveOfferingRecyclerView = findViewById(R.id.rv_exclusive_offering)
        searchEditText = findViewById(R.id.et_search)
        popularNowText = findViewById(R.id.tv_popular_now) // TextView for "Popular Now"
        exclusiveOfferingText = findViewById(R.id.tv_exclusive_offering) // TextView for "Exclusive Offering"

        // Set up RecyclerViews
        popularNowRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        exclusiveOfferingRecyclerView.layoutManager = GridLayoutManager(this, 2)

        // Initialize adapters
        popularNowAdapter = ItemAdapter(emptyList(), { item ->
            navigateToItemDetail(item)
        }, { item ->
            // Handle add to cart click if needed
        })

        exclusiveOfferingAdapter = ExclusiveOfferingAdapter(this, emptyList(), { item ->
            navigateToItemDetail(item)
        }, { item ->
            // Handle add to cart click if needed
        })

        popularNowRecyclerView.adapter = popularNowAdapter
        exclusiveOfferingRecyclerView.adapter = exclusiveOfferingAdapter

        // Load initial items
        loadPopularNowItems()
        loadExclusiveOfferingItems()

        // Set up BottomNavigationView
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

        // Set up search functionality
        searchEditText.setOnEditorActionListener { _, _, _ ->
            val query = searchEditText.text.toString()
            if (query.isBlank()) {
                // If search is empty, load all items again
                restoreOriginalItems()
            } else {
                performSearch(query)
            }
            true
        }
    }

    private fun loadPopularNowItems() {
        db.collection("products")
            .orderBy("price", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .limit(10)
            .get()
            .addOnSuccessListener { documents ->
                allPopularItems = documents.toObjects(Item::class.java)
                popularNowAdapter.updateItems(allPopularItems)
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to load popular items: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }

    private fun loadExclusiveOfferingItems() {
        db.collection("products")
            .get()
            .addOnSuccessListener { documents ->
                allExclusiveItems = documents.toObjects(Item::class.java)
                exclusiveOfferingAdapter.updateItems(allExclusiveItems)
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to load exclusive offerings: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }

    private fun performSearch(query: String) {
        val searchQuery = query.lowercase(Locale.getDefault())

        // Filter popular items based on case-insensitive name match
        val filteredPopularItems = allPopularItems.filter { it.name.lowercase(Locale.getDefault()).contains(searchQuery) }

        // Update the popular now adapter with the filtered data
        popularNowAdapter.updateItems(filteredPopularItems)

        // Hide exclusive offerings during search
        exclusiveOfferingRecyclerView.visibility = RecyclerView.GONE
        exclusiveOfferingText.visibility = TextView.GONE

        // Hide Popular Now text during search if no items are found
        if (filteredPopularItems.isEmpty()) {
            popularNowText.visibility = TextView.GONE
        } else {
            popularNowText.visibility = TextView.VISIBLE
        }

        // Show Popular Now RecyclerView during search
        popularNowRecyclerView.visibility = RecyclerView.VISIBLE
    }

    private fun restoreOriginalItems() {
        // Restore all popular items and exclusive offerings when search is empty
        popularNowAdapter.updateItems(allPopularItems)
        exclusiveOfferingAdapter.updateItems(allExclusiveItems)

        // Show both lists
        popularNowRecyclerView.visibility = RecyclerView.VISIBLE
        exclusiveOfferingRecyclerView.visibility = RecyclerView.VISIBLE
        popularNowText.visibility = TextView.VISIBLE
        exclusiveOfferingText.visibility = TextView.VISIBLE
    }

    private fun navigateToItemDetail(item: Item) {
        val intent = Intent(this, ItemDetailActivity::class.java).apply {
            putExtra("name", item.name)
            putExtra("price", item.price.toString())
            putExtra("stock", item.stock.toString())
            putExtra("imageUrl", item.imageUrl)
        }
        startActivity(intent)
    }
}
