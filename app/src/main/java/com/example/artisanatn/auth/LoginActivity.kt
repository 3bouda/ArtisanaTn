package com.example.artisanatn.auth

import android.util.Log
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.artisanatn.BuyerMainActivity
import com.example.artisanatn.PartnerMainActivity
import com.example.artisanatn.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {

    // Firebase Authentication and Firestore instances
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize FirebaseAuth and Firestore instances
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Find views by their IDs
        val emailEditText: EditText = findViewById(R.id.email)
        val passwordEditText: EditText = findViewById(R.id.password)
        val loginButton: Button = findViewById(R.id.login_button)
        val registerLink: TextView = findViewById(R.id.tv_register_link)

        // Set up the click listener for the login button
        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            Log.d("LoginActivity", "Attempting to log in user with email: $email")

            // Attempt to sign in with email and password
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Log.d("LoginActivity", "Login successful")
                        val userId = auth.currentUser?.uid
                        if (userId != null) {
                            // Fetch the user document from Firestore
                            firestore.collection("Users").document(userId).get()
                                .addOnSuccessListener { document ->
                                    // Get the user's role from the document
                                    val role = document.getString("role")
                                    Log.d("LoginActivity", "User role: $role")
                                    // Navigate to the appropriate activity based on user role
                                    if (role == "Partner") {
                                        startActivity(Intent(this, PartnerMainActivity::class.java))
                                    } else {
                                        startActivity(Intent(this, BuyerMainActivity::class.java))
                                    }
                                    finish() // Close the LoginActivity
                                }
                                .addOnFailureListener { e ->
                                    Log.e("LoginActivity", "Failed to fetch user role", e)
                                    Toast.makeText(baseContext, "Failed to fetch user role: ${e.message}", Toast.LENGTH_SHORT).show()
                                }
                        }
                    } else {
                        Log.e("LoginActivity", "Authentication failed", task.exception)
                        Toast.makeText(baseContext, "Authentication failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        // Set up the click listener for the register link
        registerLink.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}
