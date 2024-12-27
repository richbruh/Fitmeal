package com.example.fitmeal

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ItemDetailActivity : AppCompatActivity() {

    private val firestore = FirebaseFirestore.getInstance()
    private val currentUser = FirebaseAuth.getInstance().currentUser
    private val firestoreService = FirestoreService()

    private var isFavorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.item_detail_view)

        val itemId = intent.getStringExtra("item_id") ?: ""
        val itemName = intent.getStringExtra("name") ?: "Item tidak diketahui"
        val itemPrice = intent.getStringExtra("price")?.toIntOrNull()?.toRupiahFormat()
            ?: "Harga tidak tersedia"
        val itemStock = intent.getStringExtra("stock") ?: "Stok tidak diketahui"
        val itemImageUrl = intent.getStringExtra("imageUrl") ?: ""

        findViewById<TextView>(R.id.item_name).text = itemName
        findViewById<TextView>(R.id.item_price).text = itemPrice
        findViewById<TextView>(R.id.item_stock).text = "Stock: $itemStock"

        val itemImage = findViewById<ImageView>(R.id.item_image)
        Glide.with(this)
            .load(itemImageUrl)
            .placeholder(R.drawable.placeholder_image)
            .error(R.drawable.placeholder)
            .into(itemImage)

        val favoriteButton = findViewById<ImageView>(R.id.btn_favorite)

        checkIfFavorite(itemId) { isFav ->
            isFavorite = isFav
            updateFavoriteIcon(favoriteButton, isFavorite)
        }

        favoriteButton.setOnClickListener {
            isFavorite = !isFavorite
            updateFavoriteIcon(favoriteButton, isFavorite)

            if (isFavorite) {
                addToFavorites(itemId)
            } else {
                removeFromFavorites(itemId)
            }
        }

        val backToHomeButton = findViewById<Button>(R.id.back_to_home_button)
        backToHomeButton.setOnClickListener {
            finish()
        }
    }

    private fun checkIfFavorite(itemId: String, callback: (Boolean) -> Unit) {
        currentUser?.let { user ->
            val favoritesRef = firestore.collection("favorites")
            favoritesRef
                .whereEqualTo("user_id", user.uid)
                .whereEqualTo("item_id", itemId)
                .get()
                .addOnSuccessListener { snapshot ->
                    callback(!snapshot.isEmpty)
                }
                .addOnFailureListener {
                    callback(false)
                }
        }
    }

    private fun addToFavorites(itemId: String) {
        currentUser?.let { user ->
            firestoreService.addFavoriteItem(user.uid, itemId,
                onSuccess = {
                    // Favorit berhasil ditambahkan
                },
                onFailure = { e: Exception ->
                    // Gagal menambahkan favorit
                }
            )
        }
    }

    private fun removeFromFavorites(itemId: String) {
        currentUser?.let { user ->
            firestoreService.removeFavoriteItem(user.uid, itemId,
                onSuccess = {
                    // Favorit berhasil dihapus
                },
                onFailure = { e: Exception ->
                    // Gagal menghapus favorit
                }
            )
        }
    }

    private fun updateFavoriteIcon(button: ImageView, isFavorite: Boolean) {
        if (isFavorite) {
            button.setImageResource(R.drawable.baseline_favorite_24)
        } else {
            button.setImageResource(R.drawable.baseline_favorite_border_24)
        }
    }

    private fun Int.toRupiahFormat(): String {
        return "Rp %,d".format(this).replace(',', '.')
    }
}