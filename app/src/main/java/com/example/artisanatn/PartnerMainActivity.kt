package com.example.artisanatn

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.artisanatn.auth.LoginActivity
import com.example.artisanatn.databinding.ActivityPartnerMainBinding
import com.google.firebase.auth.FirebaseAuth

// Main activity for the Partner user interface
class PartnerMainActivity : AppCompatActivity() {

    // Binding object for accessing views in the activity layout
    private lateinit var binding: ActivityPartnerMainBinding

    // FirebaseAuth instance for user authentication
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize FirebaseAuth instance
        auth = FirebaseAuth.getInstance()

        // Check if the user is currently authenticated
        if (auth.currentUser == null) {
            // If not authenticated, start LoginActivity and finish the current activity
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        // Inflate the activity's layout using view binding
        binding = ActivityPartnerMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get the BottomNavigationView from the layout
        val navView: BottomNavigationView = binding.navPartnerView

        // Get the NavController for handling navigation
        val navController = findNavController(R.id.nav_host_fragment_activity_partner_main)

        // Define the top-level destinations for the navigation
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_dashboard_partner, R.id.navigation_settings
            )
        )

        // Set up the ActionBar with the NavController
        setupActionBarWithNavController(navController, appBarConfiguration)

        // Link the BottomNavigationView with the NavController
        navView.setupWithNavController(navController)
    }
}
