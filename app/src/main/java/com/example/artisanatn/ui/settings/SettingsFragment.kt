package com.example.artisanatn.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.artisanatn.R
import com.example.artisanatn.auth.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.example.artisanatn.databinding.FragmentSettingsBinding

// Fragment for managing user settings
class SettingsFragment : Fragment() {

    // Binding object to access the layout's views
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    // FirebaseAuth instance for authentication operations
    private lateinit var auth: FirebaseAuth

    // ViewModel for managing UI-related data
    private lateinit var settingsViewModel: SettingsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Initialize the ViewModel
        settingsViewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)

        // Inflate the layout for this fragment using view binding
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Initialize FirebaseAuth instance
        auth = FirebaseAuth.getInstance()

        // Get references to the buttons in the layout
        val profileButton: Button = root.findViewById(R.id.button_profile)
        val logoutButton: Button = root.findViewById(R.id.button_logout)

        // Set up click listener for the "Profile" button to navigate to ProfileEditFragment
        profileButton.setOnClickListener {
            view?.findNavController()?.navigate(R.id.profileEditFragment)
        }

        // Set up click listener for the "Logout" button to sign out and navigate to LoginActivity
        logoutButton.setOnClickListener {
            auth.signOut()  // Sign out the current user
            startActivity(Intent(activity, LoginActivity::class.java))  // Navigate to LoginActivity
            requireActivity().finish()  // Close the current activity
        }

        // Return the root view of the binding
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Clear the binding reference to prevent memory leaks
        _binding = null
    }
}
