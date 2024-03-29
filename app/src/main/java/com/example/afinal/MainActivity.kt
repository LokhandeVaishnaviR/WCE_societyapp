package com.example.afinal


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {

    private lateinit var adminLogin : Button
    private lateinit var userLogin : Button

    data class LoanDetails(
        val Name: String,
        val password: String
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);


        adminLogin = findViewById(R.id.admin_login_button)
        userLogin = findViewById(R.id.user_login_button)

        adminLogin.setOnClickListener {
            val intent = Intent(this, AdminLogin::class.java)
            startActivity(intent)
        }

        userLogin.setOnClickListener {
            val intent = Intent(this, UserLogin::class.java)
            startActivity(intent)
        }
    }
}

