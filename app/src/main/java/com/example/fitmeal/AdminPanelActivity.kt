package com.example.fitmeal

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitmeal.databinding.AdminPanelBinding


class AdminPanelActivity : AppCompatActivity() {

    private lateinit var binding: AdminPanelBinding
    private lateinit var adapter: AdminAdapter
    private val stockList = mutableListOf<AdminStock>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AdminPanelBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Sample data
        stockList.add(AdminStock("Egg Chicken Red", "1 Basket, Price", 50000.0, 60))
        stockList.add(AdminStock("Organic Banana", "1 Comb, Price", 20000.0, 20))
        stockList.add(AdminStock("Egg Noodles", "1 pcs, Price", 25000.0, 300))

        // Set up RecyclerView
        adapter = AdminAdapter(stockList)
        binding.recyclerViewProducts.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewProducts.adapter = adapter

        // Button Click Listeners (can be expanded based on your logic)
        binding.btnStockOpname.setOnClickListener {
            // Stock Opname logic
        }

        binding.btnRequestOrder.setOnClickListener {
            // Request Order logic
        }

        binding.btnUpdate.setOnClickListener {
            // Update logic
        }
    }
}
