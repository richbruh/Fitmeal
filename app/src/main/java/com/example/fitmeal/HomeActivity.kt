package com.example.fitmeal

import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class HomeActivity : AppCompatActivity() {

    private lateinit var popularNowRecyclerView: RecyclerView
    private lateinit var exclusiveOfferingRecyclerView: RecyclerView
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)

        // Initialize RecyclerViews
        popularNowRecyclerView = findViewById(R.id.rv_popular_now)
        exclusiveOfferingRecyclerView = findViewById(R.id.rv_exclusive_offering)

        // Set layout managers
        popularNowRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        exclusiveOfferingRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        // Load popular items
        loadPopularNowItems()
        // Load exclusive offerings
        loadExclusiveOfferingItems()
    }

    private fun loadPopularNowItems() {
        db.collection("items")
            .orderBy("likes", Query.Direction.DESCENDING)
            .limit(10)
            .get()
            .addOnSuccessListener { documents ->
                val popularItems = documents.toObjects(Item::class.java)
                val adapter = ItemAdapter(popularItems)
                popularNowRecyclerView.adapter = adapter
            }
            .addOnFailureListener { e ->
                // Handle the error
            }
    }

    private fun loadExclusiveOfferingItems() {
        db.collection("exclusive_offerings")
            .get()
            .addOnSuccessListener { documents ->
                val exclusiveItems = documents.toObjects(Item::class.java)
                val adapter = ItemAdapter(exclusiveItems)
                exclusiveOfferingRecyclerView.adapter = adapter
            }
            .addOnFailureListener { e ->
                // Handle the error
            }
    }
}

// Assuming you have a data class for Item
data class Item(
    val name: String = "",
    val price: Double = 0.0,
    val likes: Int = 0,
    val imageUrl: String = ""
)

// Assuming you have an adapter for RecyclerView
class ItemAdapter(private val itemList: List<Item>) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {
    // Adapter implementation here
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = itemList[position]
        // Bind data to view holder
    }

    override fun getItemCount(): Int = itemList.size

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // ViewHolder implementation here
    }
}
