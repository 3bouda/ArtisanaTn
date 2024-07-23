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
import com.example.artisanatn.R
import com.google.firebase.firestore.FirebaseFirestore

class EditArtisanaFragment : Fragment() {

    private val args: EditArtisanaFragmentArgs by navArgs()
    private lateinit var firestore: FirebaseFirestore
    private lateinit var editTextName: EditText
    private lateinit var editTextDescription: EditText
    private lateinit var editTextCategorie: EditText
    private lateinit var buttonSave: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_edit_artisana, container, false)
        firestore = FirebaseFirestore.getInstance()

        editTextName = view.findViewById(R.id.editTextName)
        editTextDescription = view.findViewById(R.id.editTextDescription)
        editTextCategorie = view.findViewById(R.id.editTextCategorie)
        buttonSave = view.findViewById(R.id.buttonSave)

        val artisanaId = args.artisanaId
        Log.d("artisanaId", artisanaId)
        Log.d("args", args.toString())
        firestore.collection("Artisana")
            .whereEqualTo("id", artisanaId)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val document = querySnapshot.documents.first()
                    editTextName.setText(document.getString("name"))
                    editTextDescription.setText(document.getString("description"))
                    editTextCategorie.setText(document.getString("categorie"))
                } else {
                    Log.d("Firestore", "No matching documents")
                }
            }

        buttonSave.setOnClickListener {
            val updatedData = mapOf(
                "name" to editTextName.text.toString(),
                "description" to editTextDescription.text.toString(),
                "categorie" to editTextCategorie.text.toString()
            )
            firestore.collection("Artisana").document(artisanaId).update(updatedData)
                .addOnSuccessListener {
                    // Handle success, maybe navigate back
                }
                .addOnFailureListener { e ->
                    // Handle failure
                }
        }

        return view
    }
}
