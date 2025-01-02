package com.example.fitmeal

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addUserIdFieldToUsers()
    }

    private fun addUserIdFieldToUsers() {
        db.collection("users")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val userIdString = document.getString("user_id")
                    val userIdInt = userIdString?.toIntOrNull()
                    if (userIdInt != null) {
                        db.collection("users").document(document.id)
                            .update("user_id", userIdInt)
                            .addOnSuccessListener {
                                println("User ID added successfully for document: ${document.id}")
                            }
                            .addOnFailureListener { e ->
                                println("Error adding user ID for document: ${document.id}, ${e.message}")
                            }
                    }
                }
            }
            .addOnFailureListener { e ->
                println("Error getting documents: ${e.message}")
            }
    }
}