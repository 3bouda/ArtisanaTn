package com.example.artisanatn

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.google.firebase.firestore.FirebaseFirestore

// Fragment to handle editing an existing Artisana item
class EditArtisanaFragment : Fragment() {

    // Arguments passed to this fragment, including the Artisana ID
    private val args: EditArtisanaFragmentArgs by navArgs()

    // FirebaseFirestore instance for database operations
    private lateinit var firestore: FirebaseFirestore

    // Views for editing Artisana details
    private lateinit var editTextName: EditText
    private lateinit var editTextDescription: EditText
    private lateinit var editTextCategorie: EditText
    private lateinit var buttonSave: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_edit_artisana, container, false)

        // Initialize FirebaseFirestore instance
        firestore = FirebaseFirestore.getInstance()

        // Bind views from the layout
        editTextName = view.findViewById(R.id.editTextName)
        editTextDescription = view.findViewById(R.id.editTextDescription)
        editTextCategorie = view.findViewById(R.id.editTextCategorie)
        buttonSave = view.findViewById(R.id.buttonSave)

        // Get the Artisana ID from the fragment arguments
        val artisanaId = args.artisanaId
        Log.d("artisanaId", artisanaId)
        Log.d("args", args.toString())

        // Retrieve the Artisana item from Firestore using the ID
        firestore.collection("Artisana")
            .whereEqualTo("id", artisanaId)
            .get()
            .addOnSuccessListener { querySnapshot ->
                // Check if any documents are returned
                if (!querySnapshot.isEmpty) {
                    // Get the first document and populate the fields
                    val document = querySnapshot.documents.first()
                    editTextName.setText(document.getString("name"))
                    editTextDescription.setText(document.getString("description"))
                    editTextCategorie.setText(document.getString("categorie"))
                } else {
                    // Log if no documents are found
                    Log.d("Firestore", "No matching documents")
                }
            }

        // Set up the save button click listener
        buttonSave.setOnClickListener {
            // Create a map of updated data
            val updatedData = mapOf(
                "name" to editTextName.text.toString(),
                "description" to editTextDescription.text.toString(),
                "categorie" to editTextCategorie.text.toString()
            )

            // Update the Artisana item in Firestore
            firestore.collection("Artisana").document(artisanaId).update(updatedData)
                .addOnSuccessListener {
                    // Handle success (e.g., navigate back or show a message)
                }
                .addOnFailureListener { e ->
                    // Handle failure (e.g., show an error message)
                }
        }

        // Return the inflated view
        return view
    }
}
