package com.example.fitmeal

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_page)

        // Initialize Firebase Auth and Firestore
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val emailInput = findViewById<EditText>(R.id.emailInput)
        val passwordInput = findViewById<EditText>(R.id.passwordInput)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val createAccountButton = findViewById<Button>(R.id.createAccountButton)

        loginButton.setOnClickListener {
            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                // Authenticate user with Firebase
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val userId = auth.currentUser?.uid
                            if (userId != null) {
                                // Fetch user role from Firestore
                                firestore.collection("users").document(userId)
                                    .get()
                                    .addOnSuccessListener { document ->
                                        if (document.exists()) {
                                            val role = document.getString("role")
                                            if (role == "admin") {
                                                val intent = Intent(this, DashboardActivity::class.java)
                                                startActivity(intent)
                                            } else if (role == "user") {
                                                val intent = Intent(this, HomeActivity::class.java)
                                                startActivity(intent)
                                            }
                                            finish()
                                        } else {
                                            Toast.makeText(this, "User role not found", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(this, "Failed to fetch user role", Toast.LENGTH_SHORT).show()
                                    }
                            }
                        } else {
                            Toast.makeText(this, "Login failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            }
        }

        createAccountButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}