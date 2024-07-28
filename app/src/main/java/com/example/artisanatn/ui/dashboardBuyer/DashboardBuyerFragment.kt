package com.example.artisanatn.ui.dashboardBuyer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.artisanatn.ArtisanaAdapter
import com.example.artisanatn.databinding.FragmentDashboardBuyerBinding

// Fragment to display the dashboard for buyers
class DashboardBuyerFragment : Fragment() {

    // Binding object to access the layout's views
    private var _binding: FragmentDashboardBuyerBinding? = null
    private val binding get() = _binding!!

    // ViewModel for managing UI-related data
    private lateinit var dashboardBuyerViewModel: DashboardBuyerViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment using view binding
        _binding = FragmentDashboardBuyerBinding.inflate(inflater, container, false)

        // Initialize the ViewModel
        dashboardBuyerViewModel = ViewModelProvider(this).get(DashboardBuyerViewModel::class.java)

        // Set up RecyclerView with a LinearLayoutManager
        binding.recyclerViewArtisanaBuyer.layoutManager = LinearLayoutManager(context)

        // Observe changes in the artisanatList LiveData and update the adapter
        dashboardBuyerViewModel.artisanatList.observe(viewLifecycleOwner, Observer { artisanas ->
            binding.recyclerViewArtisanaBuyer.adapter = ArtisanaAdapter(artisanas)
        })

        // Return the root view of the binding
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Clear the binding reference to prevent memory leaks
        _binding = null
    }
}
