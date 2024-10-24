package com.example.fitmeal

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fitmeal.databinding.CartItemBinding

class CartAdapter(
    private val items: List<CartItem>,
    private val listener: OnCartItemListener
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    interface OnCartItemListener {
        fun onIncreaseQuantity(item: CartItem)
        fun onDecreaseQuantity(item: CartItem)
        fun onRemoveItem(item: CartItem)
    }

    inner class CartViewHolder(private val binding: CartItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CartItem) {
            binding.tvProductName.text = item.name
            binding.tvQuantity.text = item.quantity.toString()

            // Calculate total price based on quantity
            val totalPrice = item.price * item.quantity
            binding.tvProductPrice.text = totalPrice.toString()

            binding.btnIncrease.setOnClickListener {
                listener.onIncreaseQuantity(item)
            }

            binding.btnDecrease.setOnClickListener {
                listener.onDecreaseQuantity(item)
            }

            binding.btnRemoveItem.setOnClickListener {
                listener.onRemoveItem(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = CartItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}
