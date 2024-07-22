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

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val emailEditText: EditText = findViewById(R.id.email)
        val passwordEditText: EditText = findViewById(R.id.password)
        val loginButton: Button = findViewById(R.id.login_button)
        val registerLink: TextView = findViewById(R.id.tv_register_link)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            Log.d("LoginActivity", "Attempting to log in user with email: $email")

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Log.d("LoginActivity", "Login successful")
                        val userId = auth.currentUser?.uid
                        if (userId != null) {
                            firestore.collection("Users").document(userId).get()
                                .addOnSuccessListener { document ->
                                    val role = document.getString("role")
                                    Log.d("LoginActivity", "User role: $role")
                                    if (role == "Partner") {
                                        startActivity(Intent(this, PartnerMainActivity::class.java))
                                    } else {
                                        startActivity(Intent(this, BuyerMainActivity::class.java))
                                    }
                                    finish()
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

        registerLink.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}
