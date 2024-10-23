package com.example.fitmeal

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SecurityActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.security_activity)

        // Initialize Firebase Auth and Firestore
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val securityIcon = findViewById<ImageView>(R.id.security_image)
        val btnChangePassword = findViewById<Button>(R.id.btn_change_password)

        val userId = auth.currentUser?.uid
        if (userId != null) {
            // Fetch user security data from Firestore
            firestore.collection("user").document(userId)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        // Handle the fetched data (if any)
                        // For example, you can set the security icon or other UI elements
                    } else {
                        // Handle the case where the document does not exist
                    }
                }
                .addOnFailureListener { exception ->
                    // Handle the error
                }
        }

        btnChangePassword.setOnClickListener {
            // Handle change password action
        }
    }
}