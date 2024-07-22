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

class PartnerMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPartnerMainBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()
        if (auth.currentUser == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        binding = ActivityPartnerMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navPartnerView

        val navController = findNavController(R.id.nav_host_fragment_activity_partner_main)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_dashboard_partner, R.id.navigation_settings
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }
}
