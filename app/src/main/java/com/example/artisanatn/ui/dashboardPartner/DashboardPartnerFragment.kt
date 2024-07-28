package com.example.artisanatn.ui.dashboardPartner

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.artisanatn.ArtisanaAdapter
import com.example.artisanatn.R
import com.example.artisanatn.databinding.FragmentDashboardPartnerBinding

// Fragment to display the dashboard for partners
class DashboardPartnerFragment : Fragment() {

    // Binding object to access the layout's views
    private var _binding: FragmentDashboardPartnerBinding? = null
    private val binding get() = _binding!!

    // ViewModel for managing UI-related data
    private lateinit var dashboardPartnerViewModel: DashboardPartnerViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment using view binding
        _binding = FragmentDashboardPartnerBinding.inflate(inflater, container, false)

        // Initialize the ViewModel
        dashboardPartnerViewModel = ViewModelProvider(this).get(DashboardPartnerViewModel::class.java)

        // Set up RecyclerView with a LinearLayoutManager
        binding.recyclerViewArtisana.layoutManager = LinearLayoutManager(context)

        // Observe changes in the artisanatList LiveData and update the adapter
        dashboardPartnerViewModel.artisanatList.observe(viewLifecycleOwner, Observer { artisanas ->
            binding.recyclerViewArtisana.adapter = ArtisanaAdapter(artisanas)
        })

        // Set up a click listener for the "Add Artisana" button to navigate to the AddArtisana fragment
        binding.buttonAddArtisanat.setOnClickListener {
            it.findNavController().navigate(R.id.action_dashboardPartner_to_addArtisana)
        }

        // Return the root view of the binding
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Clear the binding reference to prevent memory leaks
        _binding = null
    }
}
