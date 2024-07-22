package com.example.artisanatn.ui.dashboardPartner

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.artisanatn.Artisana
import com.example.artisanatn.ArtisanaAdapter
import com.example.artisanatn.R
import com.example.artisanatn.databinding.FragmentDashboardPartnerBinding
import com.example.artisanatn.ui.dashboardBuyer.DashboardPartnerViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class DashboardPartnerFragment : Fragment() {

    private var _binding: FragmentDashboardPartnerBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var artisanaAdapter: ArtisanaAdapter
    private val artisanas = mutableListOf<Artisana>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardPartnerViewModel =
            ViewModelProvider(this).get(DashboardPartnerViewModel::class.java)

        _binding = FragmentDashboardPartnerBinding.inflate(inflater, container, false)
        val root: View = binding.root
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val addArtisanatButton: Button = root.findViewById(R.id.button_add_artisanat)
        addArtisanatButton.setOnClickListener {
            view?.findNavController()?.navigate(R.id.add_artisanat)
        }

        val recyclerView: RecyclerView = root.findViewById(R.id.recyclerViewArtisana)
        artisanaAdapter = ArtisanaAdapter(artisanas)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = artisanaAdapter

        fetchArtisanaData()

        return root
    }

    private fun fetchArtisanaData() {
        val user = auth.currentUser
        val userId = user?.uid

        if (userId != null) {
            firestore.collection("Artisana")
                .whereEqualTo("userUid", userId)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val name = document.getString("name") ?: ""
                        val description = document.getString("description") ?: ""
                        val categorie = document.getString("categorie") ?: ""
                        val artisana = Artisana(name, description, categorie)
                        artisanas.add(artisana)
                    }
                    artisanaAdapter.notifyDataSetChanged()
                }
                .addOnFailureListener { exception ->
                    // Handle any errors
                }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
