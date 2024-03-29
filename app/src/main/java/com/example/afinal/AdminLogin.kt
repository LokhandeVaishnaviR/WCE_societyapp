package com.example.afinal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AdminLogin : AppCompatActivity() {

    private lateinit var usernameEditText: TextInputEditText
    private lateinit var passwordEditText: TextInputEditText
    private lateinit var login : Button

    private val collectionName = "login"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_login)

        val db = Firebase.firestore

        usernameEditText = findViewById(R.id.username_edit_text)
        passwordEditText = findViewById(R.id.password_edit_text)
        login = findViewById<Button>(R.id.login_button)


        login.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Query Firestore to check if the username exists and if the password matches
            db.collection(collectionName)
                .whereEqualTo("username", username)
                .get()
                .addOnSuccessListener { documents ->
                    if (documents.isEmpty) {
                        // Username not found
                        Toast.makeText(this, "Username not found", Toast.LENGTH_SHORT).show()
                    } else {
                        val user = documents.first()
                        val storedPassword = user.getString("password")
                        if (storedPassword == password) {
                            // Password matches
                            Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                            // Start new activity or do whatever you want after successful login
                            // Example:
                             val intent = Intent(this, AdminOperations::class.java)
                             startActivity(intent)
                        } else {
                            // Password doesn't match
                            Toast.makeText(this, "Incorrect password", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                .addOnFailureListener { e ->
                    // Handle failures
                    Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
