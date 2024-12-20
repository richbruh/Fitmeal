package com.example.fitmeal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class AdminItemAdapter(
    private val items: List<Item>,
    private val onUploadImageClicked: (Int) -> Unit,
    private val onSubmitProductClicked: (Item) -> Unit
) : RecyclerView.Adapter<AdminItemAdapter.AdminItemViewHolder>() {

    inner class AdminItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productImage: ImageView = itemView.findViewById(R.id.productImage)
        val btnUploadImage: Button = itemView.findViewById(R.id.btnUploadImage)
        val productName: EditText = itemView.findViewById(R.id.productName)
        val productStock: EditText = itemView.findViewById(R.id.productStock)
        val productPrice: EditText = itemView.findViewById(R.id.productPrice)
        val btnSubmitProduct: Button = itemView.findViewById(R.id.btnSubmitProduct)

        fun bind(item: Item) {
            productName.setText(item.name)
            productStock.setText(item.stock.toString())
            productPrice.setText(item.price.toString())
            Glide.with(itemView.context)
                .load(item.imageUrl)
                .placeholder(R.drawable.placeholder_image)
                .into(productImage)

            btnUploadImage.setOnClickListener {
                onUploadImageClicked(adapterPosition)
            }

            btnSubmitProduct.setOnClickListener {
                item.name = productName.text.toString()
                item.stock = productStock.text.toString().toInt()
                item.price = productPrice.text.toString().toInt()
                onSubmitProductClicked(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.admin_item, parent, false)
        return AdminItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdminItemViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}