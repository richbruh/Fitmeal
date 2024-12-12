package com.example.fitmeal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView

class ItemAdapter(private val itemList: List<Item>, private val fragmentContainerId: Int) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemName: TextView = itemView.findViewById(R.id.itemName)
        val itemPrice: TextView = itemView.findViewById(R.id.itemPrice)
        val itemStock: TextView = itemView.findViewById(R.id.itemStock)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = itemList[position]
                    val bundle = Bundle().apply {
                        putString("name", item.name)
                        putString("price", item.price.toString())
                        putString("stock", item.stock.toString())
                        putString("imageUrl", item.imageUrl)
                    }
                    val fragment = ItemDetailFragment().apply {
                        arguments = bundle
                    }
                    (itemView.context as AppCompatActivity).supportFragmentManager.beginTransaction()
                        .replace(fragmentContainerId, fragment)
                        .addToBackStack(null)
                        .commit()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.favorite_item, parent, false)
        return ItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val currentItem = itemList[position]
        holder.itemName.text = currentItem.name
        holder.itemPrice.text = currentItem.price.toString()
        holder.itemStock.text = currentItem.stock.toString()
    }

    override fun getItemCount() = itemList.size
}