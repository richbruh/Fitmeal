package com.example.fitmeal

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
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

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val cartItem = cartItems[position]
        val item = itemList.find { it.itemID == cartItem.itemID }

        if (item != null) {
            holder.binding.tvProductName.text = item.name
            holder.binding.tvProductPrice.text = item.price.toRupiahFormat()
            holder.binding.tvQuantity.text = "Stock: " + cartItem.quantity.toString()
            holder.binding.tvCategory.text = item.category.name
            Glide.with(holder.binding.imgProduct.context)
                .load(item.imageUrl)
                .placeholder(R.drawable.placeholder_image)
                .into(holder.binding.imgProduct)
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

    private fun Int.toRupiahFormat(): String {
        return "Rp %,d".format(this).replace(',', '.')
    }

    class CartViewHolder(val binding: CartItemBinding) : RecyclerView.ViewHolder(binding.root)
}