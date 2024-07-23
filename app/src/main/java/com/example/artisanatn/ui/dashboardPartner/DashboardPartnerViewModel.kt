package com.example.artisanatn.ui.dashboardPartner

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.artisanatn.Artisana
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth

class DashboardPartnerViewModel : ViewModel() {

    private val _artisanatList = MutableLiveData<List<Artisana>>()
    val artisanatList: LiveData<List<Artisana>> = _artisanatList

    init {
        loadArtisanaData()
    }

    private fun loadArtisanaData() {
        val auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser?.uid
        val firestore = FirebaseFirestore.getInstance()

        userId?.let {
            firestore.collection("Artisana")
                .whereEqualTo("userUid", userId)
                .get()
                .addOnSuccessListener { result ->
                    val artisanatList = result.map { document ->
                        Artisana(
                            id = document.getString("id") ?: "",
                            name = document.getString("name") ?: "",
                            description = document.getString("description") ?: "",
                            categorie = document.getString("categorie") ?: ""
                        )
                    }
                    _artisanatList.value = artisanatList
                }
        }
    }
}
