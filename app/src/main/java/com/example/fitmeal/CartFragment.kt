package com.example.fitmeal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitmeal.databinding.FragmentCartBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class CartFragment : Fragment() {

    private lateinit var binding: FragmentCartBinding
    private lateinit var cartAdapter: CartAdapter
    private val cartItems = mutableListOf<CartItem>()
    private val itemList = mutableListOf<Item>() // List of all items
    private val currentCartID = 1 // This should be dynamically set based on the logged-in user

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCartBinding.inflate(inflater, container, false)

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
            layoutManager = LinearLayoutManager(requireContext())
            adapter = cartAdapter
        }

        binding.btnCheckout.setOnClickListener {
            Toast.makeText(requireContext(), "Proceed to checkout", Toast.LENGTH_SHORT).show()
        }

        val backButton = binding.btnBack
        backButton.setOnClickListener {
            requireActivity().onBackPressed()
        }

        return binding.root
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
