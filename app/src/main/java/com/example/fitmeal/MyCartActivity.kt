package com.example.fitmeal

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitmeal.databinding.ActivityMyCartBinding

class MyCartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyCartBinding
    private lateinit var cartAdapter: CartAdapter
    private val cartItems = mutableListOf<CartItem>()
    private val itemList = mutableListOf<Item>()
    private val currentCartID = "1" // Dynamically set based on the logged-in user

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadItems()
        setupRecyclerView()

        binding.btnCheckout.setOnClickListener {
            Toast.makeText(this, "Proceed to checkout", Toast.LENGTH_SHORT).show()
        }

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setupRecyclerView() {
        cartAdapter = CartAdapter(cartItems, itemList, object : CartAdapter.OnCartItemListener {
            override fun onIncreaseQuantity(item: CartItem) {
                val index = cartItems.indexOf(item)
                if (index >= 0) {
                    item.quantity++
                    cartAdapter.notifyItemChanged(index)
                }
            }

            override fun onDecreaseQuantity(item: CartItem) {
                val index = cartItems.indexOf(item)
                if (index >= 0 && item.quantity > 1) {
                    item.quantity--
                    cartAdapter.notifyItemChanged(index)
                }
            }

            override fun onRemoveItem(item: CartItem) {
                val index = cartItems.indexOf(item)
                if (index >= 0) {
                    cartItems.removeAt(index)
                    cartAdapter.notifyItemRemoved(index)
                }
            }
        })

        binding.recyclerViewCart.apply {
            layoutManager = LinearLayoutManager(this@MyCartActivity)
            adapter = cartAdapter
        }
    }

    private fun loadItems() {
        itemList.addAll(
            listOf(
                Item(1, "Bell Pepper Red", "Details", 22000, 10, "imageUrl"),
                Item(2, "Egg Chicken Red", "Details", 50000, 20, "imageUrl"),
                Item(3, "Organic Banana", "Details", 20000, 30, "imageUrl"),
                Item(4, "Egg Noodles", "Details", 25000, 40, "imageUrl"),
                Item(5, "Egg Pasta", "Details", 22000, 50, "imageUrl")
            )
        )
    }

    fun addItemToCart(itemID: Int) {
        val existingItem = cartItems.find { it.itemID == itemID && it.cartID == currentCartID }
        if (existingItem != null) {
            val index = cartItems.indexOf(existingItem)
            existingItem.quantity++
            cartAdapter.notifyItemChanged(index)
        } else {
            cartItems.add(CartItem(currentCartID, itemID, 1))
            cartAdapter.notifyItemInserted(cartItems.size - 1)
        }
    }
}