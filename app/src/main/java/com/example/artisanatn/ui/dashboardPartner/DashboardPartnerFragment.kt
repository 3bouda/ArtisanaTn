package com.example.artisanatn.ui.dashboardPartner

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.artisanatn.databinding.FragmentDashboardPartnerBinding
import com.example.artisanatn.ui.dashboardBuyer.DashboardPartnerViewModel

class DashboardPartnerFragment : Fragment() {

    private var _binding: FragmentDashboardPartnerBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardPartnerViewModel =
            ViewModelProvider(this).get(DashboardPartnerViewModel::class.java)

        _binding = FragmentDashboardPartnerBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textDashboard
        dashboardPartnerViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}