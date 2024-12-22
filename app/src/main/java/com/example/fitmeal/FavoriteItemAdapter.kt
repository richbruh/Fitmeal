package com.example.fitmeal

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class FavoriteItemAdapter(
    private val context: Context,
    private val items: List<Favorite>
) : RecyclerView.Adapter<FavoriteItemAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemName: TextView = itemView.findViewById(R.id.itemName)
        val itemPrice: TextView = itemView.findViewById(R.id.itemPrice)
        val itemImage: ImageView = itemView.findViewById(R.id.itemImage)

        fun bind(item: Favorite) {
            itemName.text = item.name
            itemPrice.text = "Rp${item.price}"
            Glide.with(context)
                .load(item.imageUrl)
                .placeholder(R.drawable.placeholder_image)
                .into(itemImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.favorite_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}