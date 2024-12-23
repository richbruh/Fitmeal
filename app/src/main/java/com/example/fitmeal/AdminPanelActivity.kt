package com.example.fitmeal

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitmeal.databinding.ActivityAdminPanelBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class AdminPanelActivity : AppCompatActivity(), AdminActions {

    private lateinit var binding: ActivityAdminPanelBinding
    private lateinit var adapter: AdminItemAdapter
    private val itemList = mutableListOf<Item>()
    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private var selectedPosition: Int = -1

    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val imageUri = result.data?.data
            if (imageUri != null && selectedPosition >= 0) {
                uploadImageToStorage(imageUri, selectedPosition)
            } else {
                Toast.makeText(this, "No image selected.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminPanelBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = AdminItemAdapter(itemList, { position ->
            openGalleryForImage(position)
        }, { item ->
            updateItemInFirestore(item)
        }, { position ->
            removeItem(position)
        })

        binding.recyclerViewProducts.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewProducts.adapter = adapter

        binding.btnAddNewItem.setOnClickListener {
            addNewItem()
        }

        verifyAuthentication()
        loadItems()
    }

    private fun addNewItem() {
        val newItem = Item(
            itemID = UUID.randomUUID().hashCode(),
            name = "",
            stock = 0,
            price = 0,
            category = CharCategory.UNCATEGORIZED,
            imageUrl = ""
        )
        itemList.add(newItem)
        adapter.notifyItemInserted(itemList.size - 1)
    }

    private fun loadItems() {
        db.collection("products")
            .get()
            .addOnSuccessListener { documents ->
                val items = documents.toObjects(Item::class.java)
                itemList.clear()
                itemList.addAll(items)
                adapter.notifyItemRangeInserted(0, items.size) // Use a specific method
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to load items: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }

    private fun updateItemInFirestore(item: Item) {
        db.collection("products").document(item.itemID.toString())
            .set(item)
            .addOnSuccessListener {
                Toast.makeText(this, "Product updated successfully!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to update product: ${e.message}", Toast.LENGTH_SHORT)
                    .show()
            }
    }

    override fun openGalleryForImage(position: Int) {
        selectedPosition = position
        val intent = Intent(Intent.ACTION_PICK).apply { type = "image/*" }
        pickImageLauncher.launch(intent)
    }


    override fun removeItem(position: Int) {
        val item = itemList[position]
        val itemRef = db.collection("products").document(item.itemID.toString())
        val imageRef = storage.getReferenceFromUrl(item.imageUrl)

        // Delete the image from storage
        imageRef.delete().addOnSuccessListener {
            // Delete the item from Firestore
            itemRef.delete().addOnSuccessListener {
                itemList.removeAt(position)
                adapter.notifyItemRemoved(position)
                Toast.makeText(this, "Item removed successfully!", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener { e ->
                Toast.makeText(this, "Failed to remove item: ${e.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        }.addOnFailureListener { e ->
            Toast.makeText(this, "Failed to delete image: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun uploadImageToStorage(imageUri: Uri, position: Int) {
        val storageRef = storage.reference.child("product_images/${UUID.randomUUID()}.jpg")
        storageRef.putFile(imageUri)
            .addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    itemList[position] = itemList[position].copy(imageUrl = uri.toString())
                    adapter.notifyItemChanged(position)
                    Toast.makeText(this, "Image updated successfully!", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to upload image: ${e.message}", Toast.LENGTH_SHORT)
                    .show()
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
                            Toast.makeText(this, "Access denied. Admin only.", Toast.LENGTH_SHORT)
                                .show()
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
}