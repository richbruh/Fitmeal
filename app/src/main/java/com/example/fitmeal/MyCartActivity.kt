package com.example.fitmeal

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitmeal.databinding.ActivityMyCartBinding
import com.google.firebase.auth.FirebaseAuth
import java.util.UUID

class MyCartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyCartBinding
    private lateinit var cartAdapter: CartAdapter
    private val firestoreService = FirestoreService()
    private var currentCart: Cart? = null
    private val itemList = mutableListOf<Item>()
    private val cartItems = mutableListOf<CartItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: run {
            Toast.makeText(this, "User not logged in!", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        setupRecyclerView()
        fetchUserCart(userId)

        binding.btnCheckout.setOnClickListener {
            Toast.makeText(this, "Proceed to checkout", Toast.LENGTH_SHORT).show()
        }

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun fetchUserCart(userId: String) {
        println("Fetching cart for userId: $userId") // Verifikasi UID asli
        firestoreService.getCartByUserId(userId, { cart ->
            if (cart != null) {
                println("Cart fetched: ${cart.cartID}")
                currentCart = cart
                cartItems.clear()
                cartItems.addAll(cart.items)
                cartAdapter.notifyDataSetChanged()
            } else {
                println("No cart found for userId: $userId")
                createNewCart(userId)
            }
        }, { e ->
            println("Error fetching cart: ${e.message}")
            Toast.makeText(this, "Error fetching cart: ${e.message}", Toast.LENGTH_SHORT).show()
        })
    }

    private fun createNewCart(userId: String) {
        val newCart = Cart(
            cartID = UUID.randomUUID().toString(), user_id = userId, // Gunakan UID asli
            createdAt = System.currentTimeMillis(), items = mutableListOf()
        )
        println("Creating cart: $newCart") // Log data untuk memastikan isinya benar
        currentCart = newCart

        firestoreService.addOrUpdateCart(newCart, {
            Toast.makeText(this, "New cart created!", Toast.LENGTH_SHORT).show()
        }, { e ->
            println("Error creating cart: ${e.message}") // Log error untuk debugging
            Toast.makeText(this, "Error creating cart: ${e.message}", Toast.LENGTH_SHORT).show()
        })
    }

    private fun addItemToCart(item: Item) {
        // Buat keranjang baru jika belum ada
        if (currentCart == null) {
            val userId = getUserIdFromSharedPreferences() // Mengambil hash UID
            if (userId != null) {
                createNewCart(userId)
            } else {
                Toast.makeText(this, "Error: User not logged in.", Toast.LENGTH_SHORT).show()
                return
            }
        }

        val cart = currentCart ?: return

        // Cari item yang sudah ada di keranjang
        val existingItem = cart.items.find { it.itemID == item.itemID }
        if (existingItem != null) {
            existingItem.quantity++
        } else {
            cart.items.add(CartItem(itemID = item.itemID, quantity = 1))
        }

        // Perbarui keranjang di Firestore
        firestoreService.addOrUpdateCart(cart, {
            Toast.makeText(this, "${item.name} added to cart", Toast.LENGTH_SHORT).show()
            cartAdapter.notifyDataSetChanged()
        }, { e ->
            Toast.makeText(this, "Error updating cart: ${e.message}", Toast.LENGTH_SHORT).show()
        })
    }

    private fun setupRecyclerView() {
        cartAdapter = CartAdapter(cartItems, itemList, object : CartAdapter.OnCartItemListener {
            override fun onIncreaseQuantity(item: CartItem) {
                addItemToCart(Item(itemID = item.itemID))
            }

            override fun onDecreaseQuantity(item: CartItem) {
                val cart = currentCart ?: return
                val existingItem = cart.items.find { it.itemID == item.itemID }
                if (existingItem != null && existingItem.quantity > 1) {
                    existingItem.quantity--
                    firestoreService.addOrUpdateCart(cart, {
                        cartAdapter.notifyDataSetChanged()
                    }, { e ->
                        Toast.makeText(
                            this@MyCartActivity, "Error: ${e.message}", Toast.LENGTH_SHORT
                        ).show()
                    })
                }
            }

            override fun onRemoveItem(item: CartItem) {
                val cart = currentCart ?: return
                cart.items.removeIf { it.itemID == item.itemID }
                firestoreService.addOrUpdateCart(cart, {
                    cartAdapter.notifyDataSetChanged()
                }, { e ->
                    Toast.makeText(this@MyCartActivity, "Error: ${e.message}", Toast.LENGTH_SHORT)
                        .show()
                })
            }
        })

        binding.recyclerViewCart.apply {
            layoutManager = LinearLayoutManager(this@MyCartActivity)
            adapter = cartAdapter
        }
    }

    private fun getUserIdFromSharedPreferences(): String? {
        val sharedPreferences: SharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
        return sharedPreferences.getString("user_id", null) // Dapatkan UID asli
    }
}