package com.example.fitmeal

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ItemAdapter(private val itemList: List<Item>) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_view, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = itemList[position]

        // Bind data to view
        holder.itemName.text = item.name
        holder.itemPrice.text = "Rp ${item.price}"

        // Load image with Glide
        Glide.with(holder.itemView.context)
            .load(item.imageUrl)
            .placeholder(R.drawable.placeholder_image)
            .into(holder.itemImage)

        // Set listener for item click
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, ItemDetailActivity::class.java).apply {
                putExtra("name", item.name)
                putExtra("quantity", item.quantity)
                putExtra("price", item.price)
                putExtra("imageResId", item.imageUrl)
            }
            context.startActivity(intent)
        }

        // Set listener for Add to Cart button (if needed)
        holder.addToCartButton.setOnClickListener {
            // Add logic for Add to Cart
        }
    }

    override fun getItemCount(): Int = itemList.size

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemImage: ImageView = itemView.findViewById(R.id.item_image)
        val itemName: TextView = itemView.findViewById(R.id.item_name)
        val itemPrice: TextView = itemView.findViewById(R.id.item_price)
        val addToCartButton: Button = itemView.findViewById(R.id.add_to_cart_button)
    }
}