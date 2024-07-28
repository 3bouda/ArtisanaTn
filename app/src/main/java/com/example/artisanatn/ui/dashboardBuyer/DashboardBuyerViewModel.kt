package com.example.artisanatn.ui.dashboardBuyer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.artisanatn.Artisana
import com.google.firebase.firestore.FirebaseFirestore
import android.util.Log

// ViewModel for managing and providing data to the DashboardBuyerFragment
class DashboardBuyerViewModel : ViewModel() {

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
        // Get an instance of FirebaseFirestore
        val firestore = FirebaseFirestore.getInstance()

        // Query the "Artisana" collection
        firestore.collection("Artisana")
            .get()
            .addOnSuccessListener { result ->
                // Map the documents to Artisana objects
                val artisanatList = result.map { document ->
                    Artisana(
                        id = document.id,
                        name = document.getString("name") ?: "",
                        description = document.getString("description") ?: "",
                        categorie = document.getString("categorie") ?: ""
                    )
                }
                // Update the MutableLiveData with the loaded data
                _artisanatList.value = artisanatList
                // Log the list of Artisana items for debugging
                Log.d("artisanaList", artisanatList.toString())
            }
            .addOnFailureListener { exception ->
                // Log an error message if the query fails
                Log.e("DashboardBuyerViewModel", "Error getting documents: ", exception)
            }
    }
}
