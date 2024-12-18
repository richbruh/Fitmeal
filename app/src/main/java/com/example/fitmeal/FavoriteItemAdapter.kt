package com.example.fitmeal

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity

class FavoriteItemAdapter(
    private val context: Context,
    private var items: MutableList<FavoriteItem>
) : BaseAdapter() {

    override fun getCount(): Int = items.size

    override fun getItem(position: Int): Any = items[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.favorite_item, parent, false)

        val item = items[position]

        // Bind data to view
        val itemImage = view.findViewById<ImageView>(R.id.itemImage)
        val itemName = view.findViewById<TextView>(R.id.itemName)
        val itemQuantity = view.findViewById<TextView>(R.id.itemStock)
        val itemPrice = view.findViewById<TextView>(R.id.itemPrice)

        itemImage.setImageResource(item.imageResId)
        itemName.text = item.name
        itemQuantity.text = item.quantity
        itemPrice.text = "${item.price}"

        // Set OnClickListener for item
        view.setOnClickListener {
            val activity = context as FragmentActivity
            val fragment = ItemDetailFragment().apply {
                arguments = Bundle().apply {
                    putString("name", item.name)
                    putString("quantity", item.quantity)
                    putInt("price", item.price)
                    putInt("imageResId", item.imageResId)
                }
            }
            activity.supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }

        return view
    }
}