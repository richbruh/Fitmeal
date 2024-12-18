package com.example.fitmeal

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

class ItemDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.item_detail_view)

        val itemName = intent.getStringExtra("name")
        val itemStock = intent.getStringExtra("stock")
        val itemPrice = intent.getIntExtra("price", 0)
        val imageResId = intent.getStringExtra("imageResId")

        val itemImageView: ImageView = findViewById(R.id.item_image)
        val itemNameTextView: TextView = findViewById(R.id.item_name)
        val itemStockTextView: TextView = findViewById(R.id.item_stock)
        val itemPriceTextView: TextView = findViewById(R.id.item_price)

        itemNameTextView.text = itemName
        itemStockTextView.text = "Stock: $itemStock"
        itemPriceTextView.text = "Rp$itemPrice"
        Glide.with(this).load(imageResId).into(itemImageView)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}