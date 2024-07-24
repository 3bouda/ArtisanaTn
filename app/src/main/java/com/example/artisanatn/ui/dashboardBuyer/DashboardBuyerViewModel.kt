package com.example.artisanatn.ui.dashboardBuyer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.artisanatn.Artisana
import com.google.firebase.firestore.FirebaseFirestore
import android.util.Log

class DashboardBuyerViewModel : ViewModel() {

    private val _artisanatList = MutableLiveData<List<Artisana>>()
    val artisanatList: LiveData<List<Artisana>> = _artisanatList

    init {
        loadArtisanaData()
    }

    private fun loadArtisanaData() {
        val firestore = FirebaseFirestore.getInstance()

        firestore.collection("Artisana")
            .get()
            .addOnSuccessListener { result ->
                val artisanatList = result.map { document ->
                    Artisana(
                        id = document.id,
                        name = document.getString("name") ?: "",
                        description = document.getString("description") ?: "",
                        categorie = document.getString("categorie") ?: ""
                    )
                }
                _artisanatList.value = artisanatList
                Log.d("artisanaList", artisanatList.toString())
            }
            .addOnFailureListener { exception ->
                Log.e("DashboardBuyerViewModel", "Error getting documents: ", exception)
            }
    }
}
