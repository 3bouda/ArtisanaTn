package com.example.artisanatn

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.artisanatn.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class EditArtisanaFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private var artisanaId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_edit_artisana, container, false)
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        val editTextName: EditText = view.findViewById(R.id.editTextName)
        val editTextDescription: EditText = view.findViewById(R.id.editTextDescription)
        val editTextCategorie: EditText = view.findViewById(R.id.editTextCategorie)
        val buttonSave: Button = view.findViewById(R.id.buttonSave)

        artisanaId = arguments?.getString("artisanaId")

        artisanaId?.let {
            firestore.collection("Artisana").document(it).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        editTextName.setText(document.getString("name"))
                        editTextDescription.setText(document.getString("description"))
                        editTextCategorie.setText(document.getString("categorie"))
                    }
                }
        }

        buttonSave.setOnClickListener {
            val name = editTextName.text.toString()
            val description = editTextDescription.text.toString()
            val categorie = editTextCategorie.text.toString()

            val artisana = hashMapOf(
                "name" to name,
                "description" to description,
                "categorie" to categorie,
                "userUid" to auth.currentUser?.uid
            )

            // Update Firestore document
            artisanaId?.let {
                firestore.collection("Artisana").document(it).set(artisana)
            }
        }

        return view
    }
}
