package com.example.fitmeal

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fitmeal.databinding.FragmentHomeBinding
import com.google.firebase.firestore.FirebaseFirestore

class HomeFragment : Fragment() {

    private lateinit var popularNowRecyclerView: RecyclerView
    private lateinit var exclusiveOfferingRecyclerView: RecyclerView
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHomeBinding.inflate(inflater, container, false)

        // Initialize RecyclerViews
        popularNowRecyclerView = binding.rvPopularNow
        exclusiveOfferingRecyclerView = binding.rvExclusiveOffering

        // Set layout managers
        popularNowRecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        exclusiveOfferingRecyclerView.layoutManager = GridLayoutManager(activity, 2)

        // Load products from Firestore
        loadPopularNowItems()
        loadExclusiveOfferingItems()

        return binding.root
    }

    private fun loadPopularNowItems() {
        db.collection("products")
            .orderBy("price", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .limit(10)
            .get()
            .addOnSuccessListener { documents ->
                val popularItems = documents.toObjects(Item::class.java)
                if (popularItems.isNotEmpty()) {
                    val adapter = ItemAdapter(popularItems, { item ->
                        val intent = Intent(activity, ItemDetailActivity::class.java).apply {
                            putExtra("id", item.itemID.toString())
                            putExtra("name", item.name)
                            putExtra("price", item.price.toString())
                            putExtra("stock", item.stock.toString())
                            putExtra("imageUrl", item.imageUrl)
                        }
                        startActivity(intent)
                    }, { item ->
                        // Handle add to cart click if needed
                    })
                    popularNowRecyclerView.adapter = adapter
                } else {
                    Toast.makeText(activity, "No popular items found", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(activity, "Failed to load popular items: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }

    private fun loadExclusiveOfferingItems() {
        db.collection("products")
            .get()
            .addOnSuccessListener { documents ->
                val exclusiveItems = documents.toObjects(Item::class.java)
                if (exclusiveItems.isNotEmpty()) {
                    val adapter = ExclusiveOfferingAdapter(requireActivity(), exclusiveItems, { item ->
                        val intent = Intent(activity, ItemDetailActivity::class.java).apply {
                            putExtra("id", item.itemID.toString())
                            putExtra("name", item.name)
                            putExtra("price", item.price.toString())
                            putExtra("stock", item.stock.toString())
                            putExtra("imageUrl", item.imageUrl)
                        }
                        startActivity(intent)
                    }, { item ->
                        // Handle add to cart click if needed
                    })
                    exclusiveOfferingRecyclerView.adapter = adapter
                } else {
                    Toast.makeText(activity, "No exclusive offerings found", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(activity, "Failed to load exclusive offerings: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }
}