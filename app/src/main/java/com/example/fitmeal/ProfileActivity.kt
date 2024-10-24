package com.example.fitmeal

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var backBtn: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_activity)


        // Inisialisasi BottomNavigationView
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav.selectedItemId = R.id.navigation_account
        BottomNavHelper.setupBottomNav(bottomNav, this)


        // Tombol Back action
        backBtn = findViewById(R.id.btnBack)
        backBtn.setOnClickListener {
            // Pindah ke HomeActivity ketika back button ditekan
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish() // Tutup FavoritesActivity
        }

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