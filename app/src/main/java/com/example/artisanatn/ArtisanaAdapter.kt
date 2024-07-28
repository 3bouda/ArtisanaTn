package com.example.artisanatn

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.artisanatn.ui.dashboardPartner.DashboardPartnerFragmentDirections

// Data class to represent an Artisana item
data class Artisana(val id: String, val name: String, val description: String, val categorie: String)

// Adapter class for displaying a list of Artisana items in a RecyclerView
class ArtisanaAdapter(private val artisanas: List<Artisana>) :
    RecyclerView.Adapter<ArtisanaAdapter.ArtisanaViewHolder>() {

    // ViewHolder class to hold and bind views for each Artisana item
    class ArtisanaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // TextViews to display Artisana item's properties
        val nameTextView: TextView = view.findViewById(R.id.textViewName)
        val descriptionTextView: TextView = view.findViewById(R.id.textViewDescription)
        val categorieTextView: TextView = view.findViewById(R.id.textViewCategorie)
    }

    // Called when a new ViewHolder is created
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtisanaViewHolder {
        // Inflate the layout for each item in the RecyclerView
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_artisana, parent, false)
        return ArtisanaViewHolder(view)
    }

    // Called to display the data at a specified position
    override fun onBindViewHolder(holder: ArtisanaViewHolder, position: Int) {
        // Get the Artisana item for the current position
        val artisana = artisanas[position]

        // Bind the Artisana item's properties to the TextViews
        holder.nameTextView.text = artisana.name
        holder.descriptionTextView.text = artisana.description
        holder.categorieTextView.text = artisana.categorie

        // Set up click listener for the item view
        holder.itemView.setOnClickListener {
            // Log the ID of the clicked Artisana item
            Log.d("artisanaAdapter", artisana.id)

            // Create an action to navigate to the EditArtisana fragment with the item's ID
            val action = DashboardPartnerFragmentDirections
                .actionDashboardPartnerToEditArtisana(artisana.id)

            // Navigate to the EditArtisana fragment
            it.findNavController().navigate(action)
        }
    }

    // Return the total number of items in the list
    override fun getItemCount() = artisanas.size
}
