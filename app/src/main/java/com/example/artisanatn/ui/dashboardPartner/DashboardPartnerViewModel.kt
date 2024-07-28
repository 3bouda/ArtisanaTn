package com.example.artisanatn.ui.dashboardPartner

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.artisanatn.Artisana
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth

// ViewModel for managing and providing data to the DashboardPartnerFragment
class DashboardPartnerViewModel : ViewModel() {

    // MutableLiveData to hold the list of Artisana items
    private val _artisanatList = MutableLiveData<List<Artisana>>()

    // Public LiveData to expose the list of Artisana items to observers
    val artisanatList: LiveData<List<Artisana>> = _artisanatList

    init {
        // Load the Artisana data when the ViewModel is initialized
        loadArtisanaData()
    }

    // Function to load Artisana data from Firebase Firestore
    private fun loadArtisanaData() {
        // Get an instance of FirebaseAuth
        val auth = FirebaseAuth.getInstance()

        // Get the current user's ID
        val userId = auth.currentUser?.uid

        // Get an instance of FirebaseFirestore
        val firestore = FirebaseFirestore.getInstance()

        // Check if userId is not null
        userId?.let {
            // Query the "Artisana" collection where the "userUid" field matches the current user's ID
            firestore.collection("Artisana")
                .whereEqualTo("userUid", userId)
                .get()
                .addOnSuccessListener { result ->
                    // Map the documents to Artisana objects
                    val artisanatList = result.map { document ->
                        Artisana(
                            id = document.getString("id") ?: "",
                            name = document.getString("name") ?: "",
                            description = document.getString("description") ?: "",
                            categorie = document.getString("categorie") ?: ""
                        )
                    }
                    // Update the MutableLiveData with the loaded data
                    _artisanatList.value = artisanatList
                }
        }
    }
}
