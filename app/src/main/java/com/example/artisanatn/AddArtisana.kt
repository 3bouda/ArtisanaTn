package com.example.artisanatn

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

// Fragment to handle adding new Artisana items
class AddArtisana : Fragment() {

    // FirebaseAuth instance for user authentication
    private lateinit var auth: FirebaseAuth

    // FirebaseFirestore instance for database operations
    private lateinit var firestore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_artisana, container, false)

        // Initialize FirebaseAuth and FirebaseFirestore instances
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Get references to the input fields and the save button
        val editTextName: EditText = view.findViewById(R.id.editTextName)
        val editTextDescription: EditText = view.findViewById(R.id.editTextDescription)
        val editTextCategorie: EditText = view.findViewById(R.id.editTextCategorie)
        val buttonSave: Button = view.findViewById(R.id.buttonSave)

        // Get the current user and their user ID
        val user = auth.currentUser
        val userId = user?.uid

        // Set up a click listener for the save button
        buttonSave.setOnClickListener {
            // Retrieve input values from the text fields
            val name = editTextName.text.toString()
            val description = editTextDescription.text.toString()
            val categorie = editTextCategorie.text.toString()

            // Check if any field is empty or if user ID is null
            if (name.isEmpty() || description.isEmpty() || categorie.isEmpty() || userId == null) {
                return@setOnClickListener // Exit if validation fails
            }

            // Create a new document reference in the "Artisana" collection
            val documentReference = firestore.collection("Artisana").document()
            val artisanaId = documentReference.id

            // Create a map of the Artisana item data
            val artisana = hashMapOf(
                "id" to artisanaId,
                "name" to name,
                "description" to description,
                "categorie" to categorie,
                "userUid" to userId
            )

            // Add the Artisana item to the Firestore collection
            firestore.collection("Artisana").add(artisana)
                .addOnSuccessListener { documentReference ->
                    // Navigate to the DashboardPartner fragment on success
                    view?.findNavController()?.navigate(R.id.navigation_dashboard_partner)
                }
                .addOnFailureListener { e ->
                    // Handle any errors that occur
                }
        }

        // Return the inflated view
        return view
    }
}
