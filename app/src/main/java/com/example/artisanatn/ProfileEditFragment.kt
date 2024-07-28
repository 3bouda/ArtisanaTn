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

// Fragment for editing user profile details
class ProfileEditFragment : Fragment() {

    // Firebase authentication and Firestore instances
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the fragment's layout
        val view = inflater.inflate(R.layout.fragment_profile_edit, container, false)

        // Initialize FirebaseAuth and Firestore instances
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Find views by their IDs
        val editTextName: EditText = view.findViewById(R.id.editTextName)
        val editTextEmail: EditText = view.findViewById(R.id.editTextEmail)
        val buttonSave: Button = view.findViewById(R.id.buttonSave)

        // Get the current user
        val user = auth.currentUser
        val userId = user?.uid

        // If user ID is available, fetch user details from Firestore
        if (userId != null) {
            firestore.collection("Users").document(userId).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        // Set the fetched details to the EditText fields
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

        // Set up the Save button's click listener
        buttonSave.setOnClickListener {
            val name = editTextName.text.toString()
            val email = editTextEmail.text.toString()

            // If any field is empty, return without doing anything
            if (name.isEmpty() || email.isEmpty()) {
                return@setOnClickListener
            }

            // Update Firestore and Firebase Authentication details
            user?.let {
                firestore.collection("Users").document(userId!!).update("name", name, "email", email)
                    .addOnSuccessListener {
                        // If email is changed, update it in FirebaseAuth and profile
                        if (email != user.email) {
                            user.updateEmail(email).addOnCompleteListener { emailTask ->
                                if (emailTask.isSuccessful) {
                                    user.updateProfile(userProfileChangeRequest {
                                        displayName = name
                                    }).addOnCompleteListener { profileTask ->
                                        if (profileTask.isSuccessful) {
                                            // Navigate based on user role
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

    // Navigate to the appropriate screen based on the user's role
    private fun navigateBasedOnRole(userId: String) {
        firestore.collection("Users").document(userId).get()
            .addOnSuccessListener { document ->
                val role = document.getString("role")
                Log.d("ProfileEditFragment", "User role: $role")
                // Navigate to Partner or Buyer dashboard based on role
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
