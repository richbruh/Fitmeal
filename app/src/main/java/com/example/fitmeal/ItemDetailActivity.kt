package com.example.fitmeal

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ItemDetailActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.item_detail_view)

        val itemId = intent.getStringExtra("id")
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
            val item = Item(
                itemID = itemId?.toIntOrNull() ?: 0,
                name = itemName ?: "",
                price = itemPrice?.toIntOrNull() ?: 0,
                stock = itemStock?.toIntOrNull() ?: 0,
                imageUrl = itemImageUrl ?: ""
            )
            addToFavorites(item)
        }
    }

    private fun addToFavorites(item: Item) {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            val userId = user.uid

            val favoriteItem = hashMapOf(
                "user_id" to userId,
                "item_id" to item.itemID,
                "name" to item.name,
                "price" to item.price,
                "imageUrl" to item.imageUrl
            )
            db.collection("favorites")
                .document("${userId}_${item.itemID}")
                .set(favoriteItem)
                .addOnSuccessListener {
                    Toast.makeText(this, "Added to favorites", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Failed to add to favorites: ${e.message}", Toast.LENGTH_LONG).show()
                }
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
            // Optionally, redirect to login activity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}