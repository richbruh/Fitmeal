package com.example.fitmeal

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ExclusiveOfferingAdapter(
    private val context: Context,
    private val items: List<Item>,
    private val onItemClicked: (Item) -> Unit,
    private val onAddToCartClicked: (Item) -> Unit
) : RecyclerView.Adapter<ExclusiveOfferingAdapter.ItemViewHolder>() {

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemName: TextView = itemView.findViewById(R.id.itemName)
        val itemPrice: TextView = itemView.findViewById(R.id.itemPrice)
        val itemStock: TextView = itemView.findViewById(R.id.itemStock)
        val itemImage: ImageView = itemView.findViewById(R.id.itemImage)
        val addToCartButton: View = itemView.findViewById(R.id.addToCartButton)

        fun bind(item: Item) {
            itemName.text = item.name
            itemPrice.text = "Rp${item.price}"
            itemStock.text = item.stock.toString()
            Glide.with(itemView.context)
                .load(item.imageUrl)
                .placeholder(R.drawable.placeholder_image)
                .into(itemImage)

            itemView.setOnClickListener {
                onItemClicked(item)
            }

            addToCartButton.setOnClickListener {
                onAddToCartClicked(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.exclusive_offering_adapter, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}