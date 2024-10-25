package com.example.fitmeal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fitmeal.R

class AdminAdapter(private val stockList: List<AdminStock>) : RecyclerView.Adapter<AdminAdapter.StockViewHolder>() {

    class StockViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productName: TextView = itemView.findViewById(R.id.productName)
        val productDetails: TextView = itemView.findViewById(R.id.productDetails)
        val productPrice: TextView = itemView.findViewById(R.id.productPrice)
        val productQuantity: TextView = itemView.findViewById(R.id.quantity)
        val btnIncreaseQuantity: Button = itemView.findViewById(R.id.btnIncreaseQuantity)
        val btnDecreaseQuantity: Button = itemView.findViewById(R.id.btnDecreaseQuantity)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_stock, parent, false)
        return StockViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: StockViewHolder, position: Int) {
        val stockItem = stockList[position]
        holder.productName.text = stockItem.productName
        holder.productDetails.text = stockItem.productDetails
        holder.productPrice.text = stockItem.productPrice.toString()
        holder.productQuantity.text = stockItem.productQuantity.toString()

        // Handling quantity increase
        holder.btnIncreaseQuantity.setOnClickListener {
            stockItem.productQuantity++
            holder.productQuantity.text = stockItem.productQuantity.toString()
        }

        // Handling quantity decrease
        holder.btnDecreaseQuantity.setOnClickListener {
            if (stockItem.productQuantity > 0) {
                stockItem.productQuantity--
                holder.productQuantity.text = stockItem.productQuantity.toString()
            }
        }
    }

    override fun getItemCount(): Int {
        return stockList.size
    }
}
