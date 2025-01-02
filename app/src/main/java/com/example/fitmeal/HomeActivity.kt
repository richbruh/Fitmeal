package com.example.fitmeal

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {
    private lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)

        // Initialize BottomNavigationView
        bottomNav = findViewById(R.id.bottom_navigation)
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_shop -> {
                    loadFragment(HomeFragment())
                    true
                }
                R.id.navigation_favorite -> {
                    loadFragment(FavoritesFragment())
                    true
                }
                R.id.navigation_account -> {
                     loadFragment(ProfileFragment())
                    true
                }
                R.id.navigation_cart -> {
                     loadFragment(CartFragment())
                    true
                }
                else -> false
            }
        }

        // Set fragment default
        loadFragment(HomeFragment())
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.commit()
    }
}