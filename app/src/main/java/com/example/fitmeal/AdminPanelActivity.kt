package com.example.fitmeal

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitmeal.databinding.AdminPanelBinding

class AdminPanelActivity : AppCompatActivity() {

    private lateinit var binding: AdminPanelBinding
    private lateinit var adapter: AdminAdapter
    private val stockList = mutableListOf<AdminStock>() // Start with an empty list

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AdminPanelBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up RecyclerView with an empty stock list
        adapter = AdminAdapter(stockList, this) // Pass this activity to the adapter for item removal
        binding.recyclerViewProducts.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewProducts.adapter = adapter

        // Button to add a new item (add a product)
        binding.btnAddNewItem.setOnClickListener {
            addNewItem() // Add a new item to the RecyclerView
        }
    }

    // Function to add a new item to the list
    private fun addNewItem() {
        // Here we add a blank new item (you can also open a dialog for the admin to fill in details)
        val newItem = AdminStock("New Product", "Product Details", 0.0, R.drawable.placeholder, 0)
        stockList.add(newItem)
        adapter.notifyItemInserted(stockList.size - 1) // Notify adapter that an item has been added
    }

    // Function to remove an item from the list
    fun removeItem(position: Int) {
        stockList.removeAt(position)
        adapter.notifyItemRemoved(position) // Notify adapter that an item has been removed
    }
}
