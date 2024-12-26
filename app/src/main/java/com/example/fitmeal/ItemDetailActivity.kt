package com.example.fitmeal

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class ItemDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.item_detail_view)

        // Menerima data dari Intent
        val itemName = intent.getStringExtra("name") ?: "Item tidak diketahui"
        val itemPrice = intent.getStringExtra("price")?.toIntOrNull()?.toRupiahFormat()
            ?: "Harga tidak tersedia"
        val itemStock = intent.getStringExtra("stock") ?: "Stok tidak diketahui"
        val itemImageUrl = intent.getStringExtra("imageUrl") ?: ""

        // Menampilkan data di UI
        findViewById<TextView>(R.id.item_name).text = itemName
        findViewById<TextView>(R.id.item_price).text = itemPrice
        findViewById<TextView>(R.id.item_stock).text = "Stok: $itemStock"

        val itemImage = findViewById<ImageView>(R.id.item_image)
        Glide.with(this)
            .load(itemImageUrl)
            .placeholder(R.drawable.placeholder_image) // Placeholder jika gambar belum siap
            .error(R.drawable.placeholder) // Gambar error jika gagal memuat
            .into(itemImage)

        // Tombol kembali ke HomeActivity
        val backToHomeButton = findViewById<Button>(R.id.back_to_home_button)
        backToHomeButton.setOnClickListener {
            finish() // Kembali ke aktivitas sebelumnya
        }
    }

    // Fungsi ekstensi untuk format Rupiah
    private fun Int.toRupiahFormat(): String {
        return "Rp %,d".format(this).replace(',', '.')
    }
}