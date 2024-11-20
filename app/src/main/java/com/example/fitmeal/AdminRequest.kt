package com.example.fitmeal

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.fitmeal.databinding.AdminRequestBinding

class AdminRequest : AppCompatActivity() {

    private lateinit var binding: AdminRequestBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AdminRequestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Example: Handle "Process" button click
        binding.btnProcessOrder.setOnClickListener {
            Toast.makeText(this, "Order Processed", Toast.LENGTH_SHORT).show()
            // Add logic to process the order here
        }

        // Example: Handle "Cancel" button click
        binding.btnCancelOrder.setOnClickListener {
            Toast.makeText(this, "Order Canceled", Toast.LENGTH_SHORT).show()
            // Add logic to cancel the order here
        }
    }
}
