package com.example.fitmeal

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore

class ItemDetailActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()

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

        val addToFavoritesButton = findViewById<Button>(R.id.add_to_favorites_button)
        addToFavoritesButton.setOnClickListener {
            addToFavorites(itemName, itemPrice, itemStock, itemImageUrl)
        }
    }

    private fun addToFavorites(name: String?, price: String?, stock: String?, imageUrl: String?) {
        if (name != null && price != null && stock != null && imageUrl != null) {
            val favoriteItem = FavoriteItem(name, stock, price.toInt(), imageUrl.hashCode())
            db.collection("favorites")
                .add(favoriteItem)
                .addOnSuccessListener {
                    Toast.makeText(this, "Added to favorites", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Failed to add to favorites: ${e.message}", Toast.LENGTH_LONG).show()
                }
        } else {
            Toast.makeText(this, "Invalid item details", Toast.LENGTH_SHORT).show()
        }
    }
}