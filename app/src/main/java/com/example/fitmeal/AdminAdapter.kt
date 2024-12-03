package com.example.fitmeal

import android.net.Uri
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class AdminAdapter(
    private val stockList: MutableList<AdminStock>, // List data stok
    private val activity: AdminPanelActivity // Referensi ke activity untuk callback
) : RecyclerView.Adapter<AdminAdapter.StockViewHolder>() {

    // ViewHolder untuk menampung elemen-elemen UI dari item_stock.xml
    class StockViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productName: EditText = itemView.findViewById(R.id.product_name)
        val productQuantity: EditText = itemView.findViewById(R.id.product_quantity)
        val productPrice: EditText = itemView.findViewById(R.id.product_price)
        val productImage: ImageView = itemView.findViewById(R.id.product_image)
        val deleteProduct: ImageView = itemView.findViewById(R.id.deleteProduct)
    }

    // Inflating layout item_stock.xml ke dalam ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_stock, parent, false)
        return StockViewHolder(itemView)
    }

    // Mengikat data ke elemen UI di setiap item
    override fun onBindViewHolder(holder: StockViewHolder, position: Int) {
        val stockItem = stockList[position]

        // Set data ke EditText
        holder.productName.setText(stockItem.name)
        holder.productQuantity.setText(stockItem.productQuantity.toString())
        holder.productPrice.setText(stockItem.price.toString())

        // Menggunakan Glide untuk memuat gambar
        if (stockItem.imageRes.isNotEmpty()) {
            Glide.with(holder.itemView.context)
                .load(stockItem.imageRes) // URL gambar atau URI
                .placeholder(R.drawable.placeholder_image) // Placeholder saat loading
                .error(R.drawable.ic_close) // Gambar jika gagal memuat
                .into(holder.productImage) // Target ImageView
        } else {
            holder.productImage.setImageResource(R.drawable.fruit) // Gambar default jika kosong
        }

        // Logging untuk debug URL gambar
        Log.d("AdminAdapter", "Image URL for position $position: ${stockItem.imageRes}")

        // Listener untuk mengupdate data lokal saat teks berubah
        holder.productName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                stockItem.name = s.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        holder.productQuantity.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                stockItem.productQuantity = s.toString().toIntOrNull() ?: 0
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        holder.productPrice.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                stockItem.price = s.toString().toDoubleOrNull() ?: 0.0
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // Listener untuk membuka galeri saat gambar diklik
        holder.productImage.setOnClickListener {
            activity.openGalleryForImage(position)
        }

        // Listener untuk menghapus item
        holder.deleteProduct.setOnClickListener {
            activity.removeItem(position)
        }
    }

    // Mengembalikan jumlah item dalam daftar
    override fun getItemCount(): Int {
        return stockList.size
    }
}
