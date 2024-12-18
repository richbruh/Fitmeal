package com.example.fitmeal

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

class ItemDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.item_detail_view)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)

        val itemName = intent.getStringExtra("name")
        val itemQuantity = intent.getStringExtra("quantity")
        val itemPrice = intent.getIntExtra("price", 0)
        val imageResId = intent.getStringExtra("imageResId")

        val itemImageView: ImageView = findViewById(R.id.item_image)
        val itemNameTextView: TextView = findViewById(R.id.item_name)
        val itemQuantityTextView: TextView = findViewById(R.id.item_quantity)
        val itemPriceTextView: TextView = findViewById(R.id.item_price)

        itemNameTextView.text = itemName
        itemQuantityTextView.text = itemQuantity
        itemPriceTextView.text = "Rp$itemPrice"
        Glide.with(this).load(imageResId).into(itemImageView)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}