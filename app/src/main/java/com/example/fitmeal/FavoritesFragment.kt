package com.example.fitmeal

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FavoritesFragment : Fragment() {

    private lateinit var favoritesRecyclerView: RecyclerView
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favoritesRecyclerView = view.findViewById(R.id.rv_favorites)
        favoritesRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        loadFavoriteItems()
    }

    private fun loadFavoriteItems() {
        val userId = auth.currentUser?.uid // Mendapatkan user_id dari pengguna yang sedang login
        if (userId != null) {
            // Ambil data favorit berdasarkan user_id
            db.collection("favorites")
                .whereEqualTo("user_id", userId) // Filter berdasarkan user_id
                .get()
                .addOnSuccessListener { documents ->
                    // Mengonversi hasil query menjadi objek Favorite
                    val favoriteItems = documents.toObjects(Favorite::class.java)

                    Log.d("Hasil", favoriteItems.toString())

                    if (favoriteItems.isNotEmpty()) {
                        // Jika ada item favorit, ambil detail produk untuk setiap item
                        fetchItemDetails(favoriteItems)
                    } else {
                        // Menampilkan pesan jika tidak ada item favorit
                        Toast.makeText(requireContext(), "No favorite items found", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { e ->
                    // Menangani kegagalan mengambil data favorit
                    Toast.makeText(requireContext(), "Failed to load favorite items: ${e.message}", Toast.LENGTH_LONG).show()
                }
        } else {
            // Menangani kasus jika pengguna belum login
            Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchItemDetails(favoriteItems: List<Favorite>) {
        val items = mutableListOf<Item>()
        for (favorite in favoriteItems) {
            db.collection("products")
                .document(favorite.item_id.toString())
                .get()
                .addOnSuccessListener { document ->
                    val item = document.toObject(Item::class.java)
                    if (item != null) {
                        items.add(item)
                        // Cek apakah semua item favorit sudah dimuat
                        if (items.size == favoriteItems.size) {
                            // Update UI setelah data favorit berhasil dimuat
                            val adapter = FavoriteItemAdapter(requireContext(), items)
                            favoritesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
                            favoritesRecyclerView.adapter = adapter
                            adapter.notifyDataSetChanged()
                        }
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(requireContext(), "Failed to load item details: ${e.message}", Toast.LENGTH_LONG).show()
                }
        }
    }
}
