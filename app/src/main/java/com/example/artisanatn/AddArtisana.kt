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

class AddArtisana : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_artisana, container, false)
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val editTextName: EditText = view.findViewById(R.id.editTextName)
        val editTextDescription: EditText = view.findViewById(R.id.editTextDescription)
        val editTextCategorie: EditText = view.findViewById(R.id.editTextCategorie)
        val buttonSave: Button = view.findViewById(R.id.buttonSave)
        val user = auth.currentUser
        val userId = user?.uid

        buttonSave.setOnClickListener {
            val name = editTextName.text.toString()
            val description = editTextDescription.text.toString()
            val categorie = editTextCategorie.text.toString()

            if (name.isEmpty() || description.isEmpty() || categorie.isEmpty() || userId == null) {
                return@setOnClickListener
            }
            val artisana = hashMapOf(
                "name" to name,
                "description" to description,
                "categorie" to categorie,
                "userUid" to userId
            )

            firestore.collection("Artisana").add(artisana)
                .addOnSuccessListener { documentReference ->
                    view?.findNavController()?.navigate(R.id.navigation_dashboard_partner)
                }
                .addOnFailureListener { e ->
                    // Handle the error
                }
        }

        return view
    }
}
