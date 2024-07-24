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

class DashboardBuyerFragment : Fragment() {

    private var _binding: FragmentDashboardBuyerBinding? = null
    private val binding get() = _binding!!
    private lateinit var dashboardBuyerViewModel: DashboardBuyerViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBuyerBinding.inflate(inflater, container, false)
        dashboardBuyerViewModel = ViewModelProvider(this).get(DashboardBuyerViewModel::class.java)

        binding.recyclerViewArtisanaBuyer.layoutManager = LinearLayoutManager(context)

        dashboardBuyerViewModel.artisanatList.observe(viewLifecycleOwner, Observer { artisanas ->
            binding.recyclerViewArtisanaBuyer.adapter = ArtisanaAdapter(artisanas)
        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
