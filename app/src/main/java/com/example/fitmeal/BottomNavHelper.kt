package com.example.fitmeal

import android.content.Context
import android.content.Intent
import com.google.android.material.bottomnavigation.BottomNavigationView

object BottomNavHelper {
    fun setupBottomNav(bottomNav: BottomNavigationView, context: Context) {
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_shop -> {
                    val intent = Intent(context, HomeActivity::class.java)
                    context.startActivity(intent)
                    true
                }
                R.id.navigation_favorite -> {
                    val intent = Intent(context, FavoritesActivity::class.java)
                    context.startActivity(intent)
                    true
                }
                R.id.navigation_account -> {
                    val intent = Intent(context, ProfileActivity::class.java)
                    context.startActivity(intent)
                    true
                }
                R.id.navigation_cart -> {
                    val intent = Intent(context, MyCartActivity::class.java)
                    context.startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }
}
