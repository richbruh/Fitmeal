package com.example.fitmeal

import android.content.Intent
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

        val itemName = intent.getStringExtra("name")
        val itemPrice = intent.getStringExtra("price")
        val itemStock = intent.getStringExtra("stock")
        val itemImageUrl = intent.getStringExtra("imageUrl")

        findViewById<TextView>(R.id.item_name).text = itemName
        findViewById<TextView>(R.id.item_price).text = itemPrice
        findViewById<TextView>(R.id.item_stock).text = itemStock
        val itemImage = findViewById<ImageView>(R.id.item_image)
        Glide.with(this)
            .load(itemImageUrl)
            .placeholder(R.drawable.placeholder_image)
            .into(itemImage)

        val backToHomeButton = findViewById<Button>(R.id.back_to_home_button)
        backToHomeButton.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
    }
}