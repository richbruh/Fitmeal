package com.example.fitmeal

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    companion object {
        private const val TAG = "LoginActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_page)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val emailInput = findViewById<EditText>(R.id.emailInput)
        val passwordInput = findViewById<EditText>(R.id.passwordInput)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val createAccountButton = findViewById<Button>(R.id.createAccountButton)
        val adminButton = findViewById<Button>(R.id.AdminButton)

        loginButton.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                loginUser(email, password)
            } else {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            }
        }

        createAccountButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        adminButton.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                loginAdmin(email, password)
            } else {
                Toast.makeText(this, "Please enter admin email and password", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid
                    if (userId != null) {
                        firestore.collection("users").document(userId)
                            .get()
                            .addOnSuccessListener { document ->
                                if (document.exists()) {
                                    val role = document.getString("role")
                                    val user = User(
                                        user_id = userId.hashCode(),
                                        username = document.getString("username") ?: "",
                                        phone_number = document.getString("phone_number") ?: "",
                                        email = email,
                                        role = role ?: ""
                                    )
                                    saveUserToSharedPreferences(user)
                                    if (role == "admin") {
                                        val intent = Intent(this, AdminPanelActivity::class.java)
                                        startActivity(intent)
                                    } else {
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
                    } else {
                        Toast.makeText(this, "Login failed: User not found", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Login failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun loginAdmin(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid
                    if (userId != null) {
                        firestore.collection("users").document(userId)
                            .get()
                            .addOnSuccessListener { document ->
                                if (document.exists()) {
                                    val role = document.getString("role")
                                    val user = User(
                                        user_id = userId.hashCode(),
                                        username = document.getString("username") ?: "",
                                        phone_number = document.getString("phone_number") ?: "",
                                        email = email,
                                        role = role ?: ""
                                    )
                                    saveUserToSharedPreferences(user)
                                    if (role == "admin") {
                                        val intent = Intent(this, AdminPanelActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                    } else {
                                        Toast.makeText(this, "Access denied: Admin only.", Toast.LENGTH_SHORT).show()
                                    }
                                } else {
                                    Toast.makeText(this, "User role not found", Toast.LENGTH_SHORT).show()
                                }
                            }
                            .addOnFailureListener {
                                Toast.makeText(this, "Failed to fetch user role", Toast.LENGTH_SHORT).show()
                            }
                    } else {
                        Toast.makeText(this, "Login failed: User not found", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Login failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun saveUserToSharedPreferences(user: User) {
        val sharedPreferences: SharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        val gson = Gson()
        val userJson = gson.toJson(user)
        editor.putString("user", userJson)
        editor.putBoolean("is_logged_in", true)
        editor.apply()
    }
}