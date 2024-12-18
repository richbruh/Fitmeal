package com.example.fitmeal

import Item
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

// Import the Item class
import com.example.fitmeal.Item

class AdminPanelActivity : AppCompatActivity() {

    private lateinit var binding: AdminPanelBinding
    private lateinit var adapter: ItemAdapter
    private val itemList = mutableListOf<Item>()
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
        adapter = ItemAdapter(itemList, { item ->
            // Handle item click
        }, { item ->
            // Handle add to cart click
        })
        binding.recyclerViewProducts.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewProducts.adapter = adapter

        // Tambahkan dekorasi antar item
        binding.recyclerViewProducts.addItemDecoration(SpaceItemDecoration(8))

        // Verifikasi autentikasi
        verifyAuthentication()

        // Setup button listeners
        setupButtonListeners()

        // Load items from Firestore
        loadItems()
    }

    private fun loadItems() {
        db.collection("products")
            .get()
            .addOnSuccessListener { documents ->
                val items = documents.toObjects(Item::class.java)
                itemList.clear()
                itemList.addAll(items)
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to load items: ${e.message}", Toast.LENGTH_LONG).show()
            }
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
        val newItem = Item(
            itemID = UUID.randomUUID().hashCode(),
            name = "",
            details = "",
            price = 0,
            stock = 0,
            imageUrl = ""
        )
        itemList.add(newItem)
        adapter.notifyItemInserted(itemList.size - 1)
    }

    private fun updateItemsToFirestore() {
        if (itemList.isEmpty()) {
            Toast.makeText(this, "No products to update.", Toast.LENGTH_SHORT).show()
            return
        }

        for (item in itemList) {
            db.collection("products").document(item.itemID.toString())
                .set(item)
                .addOnSuccessListener {
                    Log.d("AdminPanelActivity", "Product ${item.name} updated in Firestore")
                }
                .addOnFailureListener { e ->
                    Log.e("AdminPanelActivity", "Failed to update product: ${e.message}")
                }
        }
        Toast.makeText(this, "Products updated successfully!", Toast.LENGTH_SHORT).show()
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
                    itemList[position].imageUrl = uri.toString() // Update gambar produk
                    adapter.notifyItemChanged(position)
                    Toast.makeText(this, "Image updated successfully!", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to upload image: ${e.message}", Toast.LENGTH_SHORT)
                    .show()
            }
    }

    fun removeItem(position: Int) {
        if (position < 0 || position >= itemList.size) {
            Toast.makeText(this, "Invalid item position", Toast.LENGTH_SHORT).show()
            return
        }

        val removedItem = itemList[position]
        itemList.removeAt(position)
        adapter.notifyItemRemoved(position)

        db.collection("products").document(removedItem.itemID.toString())
            .delete()
            .addOnSuccessListener {
                Toast.makeText(this, "Product removed from Firestore", Toast.LENGTH_SHORT).show()
                Log.d("AdminPanelActivity", "Product removed from Firestore: ${removedItem.itemID}")
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