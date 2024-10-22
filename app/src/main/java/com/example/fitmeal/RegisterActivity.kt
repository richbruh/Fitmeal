package com.example.fitmeal

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    private lateinit var usernameInput: EditText
    private lateinit var mobileNumberInput: EditText
    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var registerButton: Button
    private lateinit var googleSignInButton: ImageView
    private lateinit var loginLink: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_page)

        // Initialize Firebase Auth and Firestore
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Menghubungkan view dengan id dari XML
        usernameInput = findViewById(R.id.usernameInput)
        mobileNumberInput = findViewById(R.id.mobileNumberInput)
        emailInput = findViewById(R.id.emailRegisterInput)
        passwordInput = findViewById(R.id.passwordRegisterInput)
        registerButton = findViewById(R.id.registerButton)
        googleSignInButton = findViewById(R.id.googleSignInButton)
        loginLink = findViewById(R.id.loginTextView)

        // Tombol Register
        registerButton.setOnClickListener {
            val username = usernameInput.text.toString().trim()
            val mobileNumber = mobileNumberInput.text.toString().trim()
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            // Validasi input
            if (TextUtils.isEmpty(username)) {
                usernameInput.error = "Please enter username"
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(mobileNumber)) {
                mobileNumberInput.error = "Please enter mobile number"
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(email)) {
                emailInput.error = "Please enter email"
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(password)) {
                passwordInput.error = "Please enter password"
                return@setOnClickListener
            }

            // Create user with email and password
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val userId = auth.currentUser?.uid
                        if (userId != null) {
                            // Create user data map
                            val userData = hashMapOf(
                                "username" to username,
                                "email" to email,
                                "role" to "user",
                                "phonenumber" to mobileNumber
                            )

                            // Store user data in Firestore
                            firestore.collection("users").document(userId)
                                .set(userData)
                                .addOnSuccessListener {
                                    Toast.makeText(this, "Account created successfully", Toast.LENGTH_SHORT).show()
                                    // Redirect to login or home activity
                                    finish()
                                }
                                .addOnFailureListener {
                                    Toast.makeText(this, "Failed to store user data", Toast.LENGTH_SHORT).show()
                                }
                        }
                    } else {
                        Toast.makeText(this, "Registration failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        // Tombol Google Sign-In (Tambahkan fungsi sesuai kebutuhan)
        googleSignInButton.setOnClickListener {
            Toast.makeText(this, "Google Sign-In Clicked", Toast.LENGTH_SHORT).show()
            // Di sini, Anda bisa menambahkan integrasi Google Sign-In
        }

        // Link Login untuk pengguna yang sudah punya akun
        loginLink.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}