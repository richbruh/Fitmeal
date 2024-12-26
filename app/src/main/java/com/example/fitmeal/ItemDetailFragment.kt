package com.example.fitmeal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment

class ItemDetailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_item_detail, container, false)

        val itemName = arguments?.getString("name")
        val itemQuantity = arguments?.getString("quantity")
        val itemPrice = arguments?.getInt("price")
        val imageResId = arguments?.getInt("imageResId")

        val itemImageView: ImageView = view.findViewById(R.id.itemImage)
        val itemNameTextView: TextView = view.findViewById(R.id.itemName)
        val itemQuantityTextView: TextView = view.findViewById(R.id.itemQuantity)
        val itemPriceTextView: TextView = view.findViewById(R.id.itemPrice)

        itemNameTextView.text = itemName
        itemQuantityTextView.text = itemQuantity
        itemPriceTextView.text = itemPrice?.toRupiahFormat()
        itemImageView.setImageResource(imageResId ?: 0)

        return view
    }

    fun Int.toRupiahFormat(): String {
        return "Rp %,d".format(this).replace(',', '.')
    }
}