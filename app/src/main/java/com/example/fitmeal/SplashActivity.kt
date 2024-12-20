package com.example.fitmeal

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)

        // Delay for 2 seconds and then transition to the appropriate activity
        Handler(Looper.getMainLooper()).postDelayed({
            val sharedPreferences: SharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
            val isLoggedIn = sharedPreferences.getBoolean("is_logged_in", false)

            val intent = if (isLoggedIn) {
                Intent(this, MainActivity::class.java)
            } else {
                Intent(this, OnboardingActivity::class.java)
            }
            startActivity(intent)
            finish()
        }, 2000) // 2000 milliseconds = 2 seconds
    }
}