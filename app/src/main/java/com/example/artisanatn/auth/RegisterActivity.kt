package com.example.artisanatn.auth

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.artisanatn.BuyerMainActivity
import com.example.artisanatn.PartnerMainActivity
import com.example.artisanatn.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {

    // Firebase Authentication and Firestore instances
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Initialize FirebaseAuth and Firestore instances
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Find views by their IDs
        val nameEditText: EditText = findViewById(R.id.name)
        val emailEditText: EditText = findViewById(R.id.email)
        val passwordEditText: EditText = findViewById(R.id.password)
        val roleSpinner: Spinner = findViewById(R.id.role_spinner)
        val registerButton: Button = findViewById(R.id.register_button)
        val loginLink: TextView = findViewById(R.id.tv_login_link)

        // Set up the role spinner with an array of roles
        ArrayAdapter.createFromResource(
            this,
            R.array.roles_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            roleSpinner.adapter = adapter
        }

        // Set up the click listener for the register button
        registerButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val role = roleSpinner.selectedItem.toString()

            // Attempt to create a new user with email and password
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Create a user document in Firestore with the provided information
                        val user = hashMapOf(
                            "name" to name,
                            "email" to email,
                            "role" to role
                        )
                        firestore.collection("Users").document(auth.currentUser!!.uid)
                            .set(user)
                            .addOnSuccessListener {
                                // Navigate to the appropriate activity based on user role
                                if (role == "Partner") {
                                    startActivity(Intent(this, PartnerMainActivity::class.java))
                                } else {
                                    startActivity(Intent(this, BuyerMainActivity::class.java))
                                }
                                finish() // Close the RegisterActivity
                            }
                            .addOnFailureListener { e ->
                                // Handle failure to save user document
                                Toast.makeText(baseContext, "Failed to save user: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                    } else {
                        // Handle registration failure
                        Toast.makeText(baseContext, "Registration failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        // Set up the click listener for the login link
        loginLink.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}
