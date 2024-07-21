package com.example.artisanatn.ui.dashboardBuyer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.artisanatn.databinding.FragmentDashboardBuyerBinding

class DashboardBuyerFragment : Fragment() {

    private var _binding: FragmentDashboardBuyerBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardBuyerViewModel =
            ViewModelProvider(this).get(DashboardBuyerViewModel::class.java)

        _binding = FragmentDashboardBuyerBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textDashboard
        dashboardBuyerViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}