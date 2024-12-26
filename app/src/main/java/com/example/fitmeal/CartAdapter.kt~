package com.example.fitmeal

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fitmeal.databinding.CartItemBinding

class CartAdapter(
    private val cartItems: List<CartItem>,
    private val itemList: List<Item>,
    private val listener: OnCartItemListener
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    interface OnCartItemListener {
        fun onIncreaseQuantity(item: CartItem)
        fun onDecreaseQuantity(item: CartItem)
        fun onRemoveItem(item: CartItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = CartItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val cartItem = cartItems[position]
        val item = itemList.find { it.itemID == cartItem.itemID }

        if (item != null) {
            holder.binding.tvProductName.text = item.name
            holder.binding.tvProductPrice.text = "Rp ${item.price}"
            holder.binding.tvQuantity.text = cartItem.quantity.toString()
            holder.binding.tvCategory.text = item.category.name
            // Load image using Glide or any other library
            // Glide.with(holder.binding.itemImage.context).load(item.imageUrl).into(holder.binding.itemImage)
        }

        holder.binding.btnIncrease.setOnClickListener {
            listener.onIncreaseQuantity(cartItem)
        }

        holder.binding.btnDecrease.setOnClickListener {
            listener.onDecreaseQuantity(cartItem)
        }

        holder.binding.btnRemoveItem.setOnClickListener {
            listener.onRemoveItem(cartItem)
        }
    }

    override fun getItemCount(): Int = cartItems.size

    class CartViewHolder(val binding: CartItemBinding) : RecyclerView.ViewHolder(binding.root)
}