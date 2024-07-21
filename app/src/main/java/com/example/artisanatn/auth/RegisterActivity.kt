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

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val nameEditText: EditText = findViewById(R.id.name)
        val emailEditText: EditText = findViewById(R.id.email)
        val passwordEditText: EditText = findViewById(R.id.password)
        val roleSpinner: Spinner = findViewById(R.id.role_spinner)
        val registerButton: Button = findViewById(R.id.register_button)
        val loginLink: TextView = findViewById(R.id.tv_login_link)

        // Set up the role spinner
        ArrayAdapter.createFromResource(
            this,
            R.array.roles_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            roleSpinner.adapter = adapter
        }

        registerButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val role = roleSpinner.selectedItem.toString()

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = hashMapOf(
                            "name" to name,
                            "email" to email,
                            "role" to role
                        )
                        firestore.collection("Users").document(auth.currentUser!!.uid)
                            .set(user)
                            .addOnSuccessListener {
                                if (role == "Partner") {
                                    startActivity(Intent(this, PartnerMainActivity::class.java))
                                } else {
                                    startActivity(Intent(this, BuyerMainActivity::class.java))
                                }
                                finish()
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(baseContext, "Failed to save user: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                    } else {
                        // If registration fails, display a message to the user
                        Toast.makeText(baseContext, "Registration failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        loginLink.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}
