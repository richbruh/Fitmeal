package com.example.fitmeal

import android.content.Intent
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fitmeal.databinding.AdminPanelBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class AdminPanelActivity : AppCompatActivity() {

    private lateinit var binding: AdminPanelBinding
    private lateinit var adapter: AdminAdapter
    private val stockList = mutableListOf<AdminStock>()
    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private var selectedPosition: Int = -1 // Menyimpan posisi item untuk penggantian gambar

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AdminPanelBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup RecyclerView
        adapter = AdminAdapter(stockList, this)
        binding.recyclerViewProducts.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewProducts.adapter = adapter

        // Tambahkan dekorasi antar item
        binding.recyclerViewProducts.addItemDecoration(SpaceItemDecoration(8))

        // Verifikasi autentikasi
        verifyAuthentication()

        // Setup button listeners
        setupButtonListeners()
    }

    private fun verifyAuthentication() {
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        if (currentUser == null) {
            Toast.makeText(this, "Session expired. Please login again.", Toast.LENGTH_SHORT).show()
            navigateToLogin()
        } else {
            val userId = currentUser.uid
            db.collection("users").document(userId)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val role = document.getString("role")
                        if (role != "admin") {
                            Toast.makeText(
                                this,
                                "Access denied. Admin only.",
                                Toast.LENGTH_SHORT
                            ).show()
                            navigateToLogin()
                        }
                    } else {
                        Toast.makeText(this, "User role not found.", Toast.LENGTH_SHORT).show()
                        navigateToLogin()
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to verify user role.", Toast.LENGTH_SHORT).show()
                    navigateToLogin()
                }
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    private fun setupButtonListeners() {
        binding.btnAddNewItem.setOnClickListener {
            addNewItem()
        }

        binding.btnUpdate.setOnClickListener {
            updateItemsToFirestore()
        }

        binding.btnStockOpname.setOnClickListener {
            Toast.makeText(this, "Stock Opname clicked", Toast.LENGTH_SHORT).show()
        }

        binding.btnRequestOrder.setOnClickListener {
            Toast.makeText(this, "Request Order clicked", Toast.LENGTH_SHORT).show()
        }
    }

    private fun addNewItem() {
        val newProduct = AdminStock(
            id = UUID.randomUUID().toString(),
            name = "New Product",
            details = "Details here",
            price = 0.0,
            productQuantity = 0,
            imageRes = "https://via.placeholder.com/150" // Placeholder image
        )
        stockList.add(newProduct)
        adapter.notifyItemInserted(stockList.size - 1)
    }

    fun openGalleryForImage(position: Int) {
        selectedPosition = position
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            val imageUri = data.data
            if (imageUri != null && selectedPosition >= 0) {
                uploadImageToStorage(imageUri, selectedPosition)
            } else {
                Toast.makeText(this, "No image selected.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun uploadImageToStorage(imageUri: Uri, position: Int) {
        val storageRef = storage.reference.child("product_images/${UUID.randomUUID()}.jpg")
        storageRef.putFile(imageUri)
            .addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    stockList[position].imageRes = uri.toString() // Update gambar produk
                    adapter.notifyItemChanged(position)
                    Toast.makeText(this, "Image updated successfully!", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to upload image: ${e.message}", Toast.LENGTH_SHORT)
                    .show()
            }
    }

    private fun updateItemsToFirestore() {
        if (stockList.isEmpty()) {
            Toast.makeText(this, "No products to update.", Toast.LENGTH_SHORT).show()
            return
        }

        for (item in stockList) {
            db.collection("products").document(item.id)
                .set(
                    mapOf(
                        "name" to item.name,
                        "details" to item.details,
                        "price" to item.price,
                        "quantity" to item.productQuantity,
                        "imageUrl" to item.imageRes
                    )
                )
                .addOnSuccessListener {
                    Log.d("AdminPanelActivity", "Product ${item.name} updated in Firestore")
                }
                .addOnFailureListener { e ->
                    Log.e("AdminPanelActivity", "Failed to update product: ${e.message}")
                }
        }
        Toast.makeText(this, "Products updated successfully!", Toast.LENGTH_SHORT).show()
    }

    fun removeItem(position: Int) {
        if (position < 0 || position >= stockList.size) {
            Toast.makeText(this, "Invalid item position", Toast.LENGTH_SHORT).show()
            return
        }

        val removedItem = stockList[position]
        stockList.removeAt(position)
        adapter.notifyItemRemoved(position)

        db.collection("products").document(removedItem.id)
            .delete()
            .addOnSuccessListener {
                Toast.makeText(this, "Product removed from Firestore", Toast.LENGTH_SHORT).show()
                Log.d("AdminPanelActivity", "Product removed from Firestore: ${removedItem.id}")
            }
            .addOnFailureListener { e ->
                Log.e("AdminPanelActivity", "Failed to remove product: ${e.message}")
                Toast.makeText(this, "Failed to remove product from Firestore", Toast.LENGTH_SHORT)
                    .show()
            }
    }

    class SpaceItemDecoration(private val space: Int) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            outRect.top = space
            outRect.bottom = space
        }
    }
}
