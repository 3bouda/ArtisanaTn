package com.example.artisanatn

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.artisanatn.ui.dashboardPartner.DashboardPartnerFragmentDirections

data class Artisana(val id: String, val name: String, val description: String, val categorie: String)

class ArtisanaAdapter(private val artisanas: List<Artisana>) :
    RecyclerView.Adapter<ArtisanaAdapter.ArtisanaViewHolder>() {

    class ArtisanaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.textViewName)
        val descriptionTextView: TextView = view.findViewById(R.id.textViewDescription)
        val categorieTextView: TextView = view.findViewById(R.id.textViewCategorie)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtisanaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_artisana, parent, false)
        return ArtisanaViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArtisanaViewHolder, position: Int) {
        val artisana = artisanas[position]
        holder.nameTextView.text = artisana.name
        holder.descriptionTextView.text = artisana.description
        holder.categorieTextView.text = artisana.categorie

        holder.itemView.setOnClickListener {
            Log.d("artisanaAdapter", artisana.id)
            val action = DashboardPartnerFragmentDirections
                .actionDashboardPartnerToEditArtisana(artisana.id)
            it.findNavController().navigate(action)
        }
    }

    override fun getItemCount() = artisanas.size
}
