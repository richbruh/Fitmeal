package com.example.fitmeal

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
    private val cartItems = mutableListOf<CartItem>()
    private val itemList = mutableListOf<Item>() // List of all items
    private val currentCartID = 1 // This should be dynamically set based on the logged-in user

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate layout using View Binding
        binding = ActivityMyCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize BottomNavigationView after setContentView
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav.selectedItemId = R.id.navigation_cart
        BottomNavHelper.setupBottomNav(bottomNav, this)

        // Load items from the database or any source
        loadItems()

        // Initialize RecyclerView
        cartAdapter = CartAdapter(cartItems, itemList, object : CartAdapter.OnCartItemListener {
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

        binding.btnCheckout.setOnClickListener {
            Toast.makeText(this, "Proceed to checkout", Toast.LENGTH_SHORT).show()
        }

        val backButton = findViewById<ImageView>(R.id.btn_back)
        backButton.setOnClickListener {
            finish()
        }
    }

    private fun loadItems() {
        // Load items from the database or any source and add them to itemList
        itemList.add(Item(1, "Bell Pepper Red", "Details", 22000, 10, "imageUrl"))
        itemList.add(Item(2, "Egg Chicken Red", "Details", 50000, 20, "imageUrl"))
        itemList.add(Item(3, "Organic Banana", "Details", 20000, 30, "imageUrl"))
        itemList.add(Item(4, "Egg Noodles", "Details", 25000, 40, "imageUrl"))
        itemList.add(Item(5, "Egg Pasta", "Details", 22000, 50, "imageUrl"))
    }

    fun addItemToCart(itemID: Int) {
        val existingItem = cartItems.find { it.itemID == itemID && it.cartID == currentCartID }
        if (existingItem != null) {
            existingItem.quantity++
        } else {
            cartItems.add(CartItem(currentCartID, itemID, 1))
        }
        cartAdapter.notifyDataSetChanged()
    }
}