package com.example.fitmeal

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdminAdapter(private val stockList: MutableList<AdminStock>, private val activity: AdminPanelActivity) : RecyclerView.Adapter<AdminAdapter.StockViewHolder>() {

    class StockViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productName: EditText = itemView.findViewById(R.id.productName)
        val productDetails: EditText = itemView.findViewById(R.id.productDetails)
        val productPrice: EditText = itemView.findViewById(R.id.productPrice)
        val productQuantity: TextView = itemView.findViewById(R.id.quantity)
        val btnIncreaseQuantity: Button = itemView.findViewById(R.id.btnIncreaseQuantity)
        val btnDecreaseQuantity: Button = itemView.findViewById(R.id.btnDecreaseQuantity)
        val deleteProduct: ImageView = itemView.findViewById(R.id.deleteProduct) // Add delete button
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_stock, parent, false)
        return StockViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: StockViewHolder, position: Int) {
        val stockItem = stockList[position]

        // Set initial values
        holder.productName.setText(stockItem.name)
        holder.productDetails.setText(stockItem.details)
        holder.productPrice.setText(stockItem.price.toString()) // Set initial price
        holder.productQuantity.text = stockItem.productQuantity.toString()

        // Handle quantity increase
        holder.btnIncreaseQuantity.setOnClickListener {
            stockItem.productQuantity++
            holder.productQuantity.text = stockItem.productQuantity.toString()
        }

        // Handle quantity decrease
        holder.btnDecreaseQuantity.setOnClickListener {
            if (stockItem.productQuantity > 0) { // Prevent going below 0
                stockItem.productQuantity--
                holder.productQuantity.text = stockItem.productQuantity.toString()
            }
        }

        // Handle item removal
        holder.deleteProduct.setOnClickListener {
            activity.removeItem(position) // Call the remove function from AdminPanelActivity
        }

        // Watch for changes in the name input
        holder.productName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                stockItem.name = s.toString() // Update the name in AdminStock
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // Watch for changes in the details input
        holder.productDetails.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                stockItem.details = s.toString() // Update the details in AdminStock
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // Watch for changes in the price input
        holder.productPrice.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val newPrice = s.toString().toDoubleOrNull() ?: stockItem.price
                stockItem.price = newPrice // Update the price in AdminStock
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    override fun getItemCount(): Int {
        return stockList.size
    }

    // Add function to allow adding a new item
    fun addItem(item: AdminStock) {
        stockList.add(item)
        notifyItemInserted(stockList.size - 1)
    }

    // Add function to allow removing an item
    fun removeItem(position: Int) {
        stockList.removeAt(position)
        notifyItemRemoved(position)
    }
}
