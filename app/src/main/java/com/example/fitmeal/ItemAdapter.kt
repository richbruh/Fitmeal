package com.example.fitmeal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ItemAdapter(
    private val items: List<Item>,
    private val onItemClicked: (Item) -> Unit,
    private val onAddToCartClicked: (Item) -> Unit
) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {
    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val itemName: TextView = itemView.findViewById(R.id.itemName)
        private val itemPrice: TextView = itemView.findViewById(R.id.itemPrice)
        private val itemStock: TextView = itemView.findViewById(R.id.itemStock)
        private val itemImage: ImageView = itemView.findViewById(R.id.itemImage)
        private val itemCategory: TextView = itemView.findViewById(R.id.itemCategory)
        private val addToCartButton: Button = itemView.findViewById(R.id.addToCartButton)

        fun bind(item: Item) {
            itemName.text = item.name
            itemPrice.text = item.price.toRupiahFormat()
            itemStock.text = "Stock: " + item.stock.toString()
            itemCategory.text = item.category.name
            Glide.with(itemView.context)
                .load(item.imageUrl)
                .placeholder(R.drawable.placeholder_image)
                .into(itemImage)
            itemView.setOnClickListener { onItemClicked(item) }
            addToCartButton.setOnClickListener { onAddToCartClicked(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_adapter, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun Int.toRupiahFormat(): String {
        return "Rp %,d".format(this).replace(',', '.')
    }
}