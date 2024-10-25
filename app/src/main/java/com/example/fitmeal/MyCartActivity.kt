package com.example.fitmeal

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitmeal.databinding.ActivityMyCartBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MyCartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyCartBinding
    private lateinit var cartAdapter: CartAdapter
    private lateinit var backBtn: ImageView
    private val cartItems = mutableListOf<CartItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate layout using View Binding
        binding = ActivityMyCartBinding.inflate(layoutInflater)
        setContentView(binding.root) // Ensure you set content using binding.root

        // Initialize BottomNavigationView after setContentView
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav.selectedItemId = R.id.navigation_cart
        BottomNavHelper.setupBottomNav(bottomNav, this)

        // Add some sample items before initializing the adapter
        cartItems.add(CartItem("Bell Pepper Red", 22000, 1, R.drawable.fruit))
        cartItems.add(CartItem("Egg Chicken Red", 50000, 1, R.drawable.fruit))
        cartItems.add(CartItem("Organic Banana", 20000, 1, R.drawable.fruit))
        cartItems.add(CartItem("Egg Noodles", 25000, 1, R.drawable.fruit))
        cartItems.add(CartItem("Egg Pasta", 22000, 1, R.drawable.fruit))

        // Initialize RecyclerView
        cartAdapter = CartAdapter(cartItems, object : CartAdapter.OnCartItemListener {
            override fun onIncreaseQuantity(item: CartItem) {
                item.quantity++
                cartAdapter.notifyDataSetChanged()
            }

            override fun onDecreaseQuantity(item: CartItem) {
                if (item.quantity > 1) {
                    item.quantity--
                    cartAdapter.notifyDataSetChanged()
                }
            }

            override fun onRemoveItem(item: CartItem) {
                cartItems.remove(item)
                cartAdapter.notifyDataSetChanged()
            }
        })

        binding.recyclerViewCart.apply {
            layoutManager = LinearLayoutManager(this@MyCartActivity)
            adapter = cartAdapter
        }

        // Notify the adapter that data has changed after adding the items
        cartAdapter.notifyDataSetChanged()

        binding.btnCheckout.setOnClickListener {
            Toast.makeText(this, "Proceed to checkout", Toast.LENGTH_SHORT).show()
        }
    }
}

