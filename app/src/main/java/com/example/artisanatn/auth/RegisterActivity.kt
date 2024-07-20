package com.example.artisanatn

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()

        val emailEditText: EditText = findViewById(R.id.email)
        val passwordEditText: EditText = findViewById(R.id.password)
        val registerButton: Button = findViewById(R.id.register_button)
        val loginLink: TextView = findViewById(R.id.tv_login_link)

        registerButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Registration success, navigate to MainActivity
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    } else {
                        // If registration fails, display a message to the user
                        Toast.makeText(baseContext, "Registration failed.", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        loginLink.setOnClickListener {
            // Navigate to LoginActivity
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}
