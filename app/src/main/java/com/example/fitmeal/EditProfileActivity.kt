package com.example.fitmeal

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class EditProfileActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var etUsername: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPhoneNumber: EditText
    private lateinit var btnUpdate: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_profile_activity)

        // Initialize Firebase Auth and Firestore
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Initialize EditTexts
        etUsername = findViewById(R.id.et_username)
        etEmail = findViewById(R.id.et_email)
        etPhoneNumber = findViewById(R.id.et_phone_number)
        btnUpdate = findViewById(R.id.btn_update)

        // Load user data when the activity starts
        loadUserData()

        // Set onClickListener for the update button
        btnUpdate.setOnClickListener {
            updateUserProfile()
        }
    }

    // Load user data from Firestore
    private fun loadUserData() {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            firestore.collection("users").document(userId)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        etUsername.setText(document.getString("username"))
                        etEmail.setText(document.getString("email"))
                        etPhoneNumber.setText(document.getString("phonenumber"))
                    }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "Error loading data: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    // Update user profile in Firestore
    private fun updateUserProfile() {
        val userId = auth.currentUser?.uid
        val username = etUsername.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val phoneNumber = etPhoneNumber.text.toString().trim()

        if (userId != null) {
            val userUpdates = hashMapOf(
                "username" to username,
                "email" to email,
                "phonenumber" to phoneNumber
            )

            firestore.collection("users").document(userId)
                .set(userUpdates) // Use set() to replace the document with new data
                .addOnSuccessListener {
                    Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                    finish() // Close the activity
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "Error updating profile: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
