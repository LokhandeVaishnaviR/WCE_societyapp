package com.example.afinal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class createUser : AppCompatActivity() {

    data class constant(
        val AccountNo : String
    )

    

    data class UserProfile(
        val AccountNo: String,
        val fullName: String,
        val email: String,
        val contactNumber: String,
        val password: String,
        val address: String,
        val department: String,
        val salary: Double
    )

    private lateinit var fullNameTextInputEditText: TextInputEditText
    private lateinit var emailTextInputEditText: TextInputEditText
    private lateinit var contactNumberTextInputEditText: TextInputEditText
    private lateinit var passwordTextInputEditText: TextInputEditText
    private lateinit var addressTextInputEditText: TextInputEditText
    private lateinit var departmentTextInputEditText: TextInputEditText
    private lateinit var salaryTextInputEditText: TextInputEditText
    private lateinit var submitButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_user)

        val db = Firebase.firestore

        fullNameTextInputEditText = findViewById(R.id.fullNameTextInputEditText)
        emailTextInputEditText = findViewById(R.id.emailTextInputEditText)
        contactNumberTextInputEditText = findViewById(R.id.contactNumberTextInputEditText)
        passwordTextInputEditText = findViewById(R.id.passwordTextInputEditText)
        addressTextInputEditText = findViewById(R.id.addressTextInputEditText)
        departmentTextInputEditText = findViewById(R.id.departmentTextInputEditText)
        salaryTextInputEditText = findViewById(R.id.salaryTextInputEditText)
        submitButton = findViewById(R.id.submitButton)

        submitButton.setOnClickListener {
            // Retrieve data from TextInputEditText fields in XML layout
            try {
                val fullName = fullNameTextInputEditText.text.toString()
                val email = emailTextInputEditText.text.toString()
                val contactNumber = contactNumberTextInputEditText.text.toString()
                val password = passwordTextInputEditText.text.toString()
                val address = addressTextInputEditText.text.toString()
                val department = departmentTextInputEditText.text.toString()
                val salary =
                    salaryTextInputEditText.text.toString().toDoubleOrNull() ?: 0.0 // Convert to Double or default to 0.0 if conversion fails

                // Fetch the most recent account number and generate the new one
                db.collection("constant")
                    .document("latestAccountNo")
                    .get()
                    .addOnSuccessListener { document ->
                        val latestAccountNo = document.getString("AccountNo")?.toIntOrNull() ?: 0
                        val newAccountNo = (latestAccountNo + 1).toString()

                        // Update the latest account number
                        db.collection("constants")
                            .document("latestAccountNo")
                            .set(mapOf("AccountNo" to newAccountNo))

                        // Create the user profile with the new account number
                        val user = UserProfile(
                            newAccountNo,
                            fullName,
                            email,
                            contactNumber,
                            password,
                            address,
                            department,
                            salary
                        )

                        // Add the user profile to Firestore
                        db.collection("Users")
                            .add(user)
                            .addOnSuccessListener { documentReference ->
                                Log.d("Info", "DocumentSnapshot added with ID: ${documentReference.id}")
                            }
                            .addOnFailureListener { e ->
                                Log.w("Info", "Error adding document", e)
                            }

                        Toast.makeText(this, user.toString(), Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { exception ->
                        Log.e("Error", "Error getting latest account number", exception)
                    }
            } catch (e : Exception){
                print(e.toString())
            }



        }
    }

    private fun generateAccountNumber(): String {
        // Generate account number based on timestamp
        val timestamp = System.currentTimeMillis()
        return "AC${timestamp}"
    }
}
