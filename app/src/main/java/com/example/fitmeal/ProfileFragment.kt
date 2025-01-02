package com.example.fitmeal

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var backBtn: ImageView
    private lateinit var tvUsername: TextView
    private lateinit var tvEmail: TextView
    private lateinit var tvPhoneNumber: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the fragment layout
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        // Initialize Firebase Auth and Firestore
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Tombol Back action
        backBtn = view.findViewById(R.id.btnBack)
        backBtn.setOnClickListener {
            // Pindah ke HomeActivity ketika back button ditekan
            val intent = Intent(activity, HomeActivity::class.java)
            startActivity(intent)
        }

        // Tombol Logout
        val btnLogout = view.findViewById<Button>(R.id.btn_logout)
        btnLogout.setOnClickListener {
            auth.signOut() // Logout dari Firebase
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish() // Tutup fragment agar tidak bisa diakses kembali setelah logout
        }

        // Initialize TextViews to display user data
        tvUsername = view.findViewById(R.id.tv_username)
        tvEmail = view.findViewById(R.id.tv_email)
        tvPhoneNumber = view.findViewById(R.id.tv_phone_number)

        // Load user data
        loadUserData(view)

        // Navigate to EditProfileActivity when "My Account" is clicked
        val tvMyAccount = view.findViewById<TextView>(R.id.tv_my_account)
        tvMyAccount.setOnClickListener {
            // Navigate to EditProfileActivity
            val intent = Intent(activity, EditProfileActivity::class.java)
            startActivity(intent)
        }
        val tvChangePassword = view.findViewById<TextView>(R.id.tv_change_password)
        tvChangePassword.setOnClickListener {
            val intent = Intent(activity, ChangePasswordActivity::class.java)
            startActivity(intent)
        }

        return view
    }

    // Load user data from Firestore
    private fun loadUserData(view: View) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            firestore.collection("users").document(userId) // Ensure the collection name is correct
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        // Get the user data from the document
                        val username = document.getString("username")
                        val email = document.getString("email")
                        val phoneNumber = document.getString("phonenumber")

                        // Set the fetched data to TextViews
                        tvUsername.text = username ?: "N/A" // Use "N/A" if null
                        tvEmail.text = email ?: "N/A"
                        tvPhoneNumber.text = phoneNumber ?: "N/A"
                    } else {
                        // Handle the case where the document does not exist
                        tvUsername.text = "User not found"
                        tvEmail.text = "N/A"
                        tvPhoneNumber.text = "N/A"
                    }
                }
                .addOnFailureListener { exception ->
                    // Handle the error (e.g., log it)
                    tvUsername.text = "Error loading data"
                    tvEmail.text = "N/A"
                    tvPhoneNumber.text = "N/A"
                }
        } else {
            // Handle case when user ID is null
            tvUsername.text = "User not logged in"
            tvEmail.text = "N/A"
            tvPhoneNumber.text = "N/A"
        }
    }

    // Refresh user data when returning to this fragment
    override fun onResume() {
        super.onResume()
        view?.let { loadUserData(it) } // Refresh user data
    }
}
