package com.example.fitmeal

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

class FavoriteItemAdapter(
    private val context: Context,
    private var items: MutableList<FavoriteItem> // Ubah menjadi MutableList agar bisa dimodifikasi
) : BaseAdapter() {

    override fun getCount(): Int = items.size

    override fun getItem(position: Int): Any = items[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.favorite_item, parent, false)

        val item = items[position]

        // Bind data ke view
        val itemImage = view.findViewById<ImageView>(R.id.itemImage)
        val itemName = view.findViewById<TextView>(R.id.itemName)
        val itemQuantity = view.findViewById<TextView>(R.id.itemQuantity)
        val itemPrice = view.findViewById<TextView>(R.id.itemPrice)
        val btnRemove = view.findViewById<ImageView>(R.id.btnRemove)

        itemImage.setImageResource(item.imageResId)
        itemName.text = item.name
        itemQuantity.text = item.quantity
        itemPrice.text = "${item.price}"

        // Menghapus item dari list ketika tombol remove ditekan
        btnRemove.setOnClickListener {
            items.removeAt(position) // Hapus item dari list
            notifyDataSetChanged()   // Update tampilan ListView
        }

        return view
    }
}
