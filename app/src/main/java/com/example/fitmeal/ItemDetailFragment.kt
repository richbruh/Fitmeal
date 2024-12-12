package com.example.fitmeal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide

class ItemDetailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_item_detail, container, false)

        // Get data from arguments
        val itemName = arguments?.getString("name")
        val itemPrice = arguments?.getString("price")
        val itemStock = arguments?.getString("stock")
        val imageUrl = arguments?.getString("imageUrl")
        val itemDescription = arguments?.getString("details")

        // Bind data to views
        val itemImageView: ImageView = view.findViewById(R.id.itemImage)
        val itemNameTextView: TextView = view.findViewById(R.id.itemName)
        val itemPriceTextView: TextView = view.findViewById(R.id.itemPrice)
        val itemDescriptionTextView: TextView = view.findViewById(R.id.itemDescription)
        val addToCartButton: Button = view.findViewById(R.id.addToCartButton)
        val shareButton: Button = view.findViewById(R.id.shareButton)

        itemNameTextView.text = itemName
        itemPriceTextView.text = "Rp$itemPrice"
        itemDescriptionTextView.text = itemDescription ?: "No description available"

        // Load image using Glide
        Glide.with(this)
            .load(imageUrl)
            .placeholder(R.drawable.sample_image)
            .into(itemImageView)

        // Add to Cart Button Action
        addToCartButton.setOnClickListener {
            // Handle Add to Cart logic here
        }

        // Share Button Action
        shareButton.setOnClickListener {
            // Handle Share logic here
        }

        return view
    }
}