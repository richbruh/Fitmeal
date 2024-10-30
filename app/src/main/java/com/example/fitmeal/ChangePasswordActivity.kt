package com.example.fitmeal

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ChangePasswordActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.change_password_activity)

        // Initialize Firebase Auth and Firestore
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val etOldPassword = findViewById<EditText>(R.id.et_old_password)
        val etNewPassword = findViewById<EditText>(R.id.et_new_password)
        val etConfirmPassword = findViewById<EditText>(R.id.et_confirm_password)
        val btnChangePassword = findViewById<Button>(R.id.btn_change_password)

        btnChangePassword.setOnClickListener {
            val oldPassword = etOldPassword.text.toString()
            val newPassword = etNewPassword.text.toString()
            val confirmPassword = etConfirmPassword.text.toString()

            if (oldPassword.isNotEmpty() && newPassword.isNotEmpty() && confirmPassword.isNotEmpty()) {
                if (newPassword == confirmPassword) {
                    val user = auth.currentUser
                    if (user != null) {
                        val credential = EmailAuthProvider.getCredential(user.email!!, oldPassword)
                        user.reauthenticate(credential)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    user.updatePassword(newPassword)
                                        .addOnCompleteListener { updateTask ->
                                            if (updateTask.isSuccessful) {
                                                Toast.makeText(this, "Password updated successfully", Toast.LENGTH_SHORT).show()
                                            } else {
                                                Toast.makeText(this, "Error updating password", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                } else {
                                    Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show()
                                }
                            }
                    }
                } else {
                    Toast.makeText(this, "New passwords do not match", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}