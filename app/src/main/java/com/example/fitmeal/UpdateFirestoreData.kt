package com.example.fitmeal

import com.google.firebase.firestore.FirebaseFirestore

fun main() {
    val db = FirebaseFirestore.getInstance()

    db.collection("users").get().addOnSuccessListener { documents ->
        for (document in documents) {
            val userId = document.id.toIntOrNull() ?: continue
            val username = document.getString("username") ?: ""
            val phoneNumber = document.getString("phonenumber") ?: ""
            val email = document.getString("email") ?: ""
            val role = document.getString("role") ?: ""

            val user = User(
                user_id = userId,
                username = username,
                phone_number = phoneNumber,
                email = email,
                role = role
            )

            db.collection("users").document(document.id).set(user)
        }
    }.addOnFailureListener { e ->
        println("Error updating documents: ${e.message}")
    }
}