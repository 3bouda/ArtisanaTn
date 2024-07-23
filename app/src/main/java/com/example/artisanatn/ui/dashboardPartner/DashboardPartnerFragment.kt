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

class DashboardPartnerFragment : Fragment() {

    private var _binding: FragmentDashboardPartnerBinding? = null
    private val binding get() = _binding!!

    private lateinit var dashboardPartnerViewModel: DashboardPartnerViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardPartnerBinding.inflate(inflater, container, false)
        dashboardPartnerViewModel = ViewModelProvider(this).get(DashboardPartnerViewModel::class.java)

        binding.recyclerViewArtisana.layoutManager = LinearLayoutManager(context)

        dashboardPartnerViewModel.artisanatList.observe(viewLifecycleOwner, Observer { artisanas ->
            binding.recyclerViewArtisana.adapter = ArtisanaAdapter(artisanas)
        })
        binding.buttonAddArtisanat.setOnClickListener {
            it.findNavController().navigate(R.id.action_dashboardPartner_to_addArtisana)
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
