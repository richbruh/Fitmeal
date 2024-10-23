package com.example.fitmeal

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_activity)

        // Initialize Firebase Auth and Firestore
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val tvUsername = findViewById<TextView>(R.id.tv_username)
        val tvEmail = findViewById<TextView>(R.id.tv_email)
        val tvPhoneNumber = findViewById<TextView>(R.id.tv_phone_number)

        val userId = auth.currentUser?.uid
        if (userId != null) {
            // Fetch user data from Firestore
            firestore.collection("user").document(userId)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val username = document.getString("username")
                        val email = document.getString("email")
                        val phoneNumber = document.getString("phone_number")

                        // Set the fetched data to TextViews
                        tvUsername.text = username
                        tvEmail.text = email
                        tvPhoneNumber.text = phoneNumber
                    } else {
                        // Handle the case where the document does not exist
                    }
                }
                .addOnFailureListener { exception ->
                    // Handle the error
                }
        }
    }
}