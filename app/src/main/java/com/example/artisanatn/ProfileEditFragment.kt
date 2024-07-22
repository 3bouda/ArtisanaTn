package com.example.artisanatn

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore

class ProfileEditFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile_edit, container, false)
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        val editTextName: EditText = view.findViewById(R.id.editTextName)
        val editTextEmail: EditText = view.findViewById(R.id.editTextEmail)
        val buttonSave: Button = view.findViewById(R.id.buttonSave)

        val user = auth.currentUser
        val userId = user?.uid

        if (userId != null) {
            firestore.collection("Users").document(userId).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val name = document.getString("name")
                        val email = document.getString("email")
                        editTextName.setText(name)
                        editTextEmail.setText(email)
                    } else {
                        Log.d("ProfileEditFragment", "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("ProfileEditFragment", "Get failed with ", exception)
                }
        }

        buttonSave.setOnClickListener {
            val name = editTextName.text.toString()
            val email = editTextEmail.text.toString()

            if (name.isEmpty() || email.isEmpty()) {
                return@setOnClickListener
            }

            user?.let {
                firestore.collection("Users").document(userId!!).update("name", name, "email", email)
                    .addOnSuccessListener {
                        if (email != user.email) {
                            user.updateEmail(email).addOnCompleteListener { emailTask ->
                                if (emailTask.isSuccessful) {
                                    user.updateProfile(userProfileChangeRequest {
                                        displayName = name
                                    }).addOnCompleteListener { profileTask ->
                                        if (profileTask.isSuccessful) {
                                            navigateBasedOnRole(userId)
                                        } else {
                                            // Handle profile update failure
                                        }
                                    }
                                } else {
                                    // Handle email update failure
                                }
                            }
                        } else {
                            // Email not changed, just update profile
                            user.updateProfile(userProfileChangeRequest {
                                displayName = name
                            }).addOnCompleteListener { profileTask ->
                                if (profileTask.isSuccessful) {
                                    // Navigate based on role
                                    navigateBasedOnRole(userId)
                                } else {
                                    // Handle profile update failure
                                }
                            }
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.d("ProfileEditFragment", "Error updating document", exception)
                    }
            }
        }

        return view
    }

    private fun navigateBasedOnRole(userId: String) {
        firestore.collection("Users").document(userId).get()
            .addOnSuccessListener { document ->
                val role = document.getString("role")
                Log.d("ProfileEditFragment", "User role: $role")
                if (role == "Partner") {
                    findNavController().navigate(R.id.navigation_dashboard_partner)
                } else {
                    findNavController().navigate(R.id.navigation_dashboard_buyer)
                }
            }
            .addOnFailureListener { exception ->
                Log.d("ProfileEditFragment", "Error getting document", exception)
            }
    }
}
