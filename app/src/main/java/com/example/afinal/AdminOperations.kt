package com.example.afinal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import androidx.cardview.widget.CardView

class AdminOperations : AppCompatActivity() {

    private lateinit var allUsersFrameLayout: CardView
    private lateinit var addUserFrameLayout: CardView
    private lateinit var updateDataFrameLayout: CardView
    private lateinit var activeLoanUsersFrameLayout: CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.admin_operations)

        allUsersFrameLayout = findViewById(R.id.all_users)
        addUserFrameLayout = findViewById(R.id.add_user)
        updateDataFrameLayout = findViewById(R.id.update_data)
        activeLoanUsersFrameLayout = findViewById(R.id.active_users)

        allUsersFrameLayout.setOnClickListener {
            val intent = Intent(this, AllUsers::class.java)
            startActivity(intent)
        }

        addUserFrameLayout.setOnClickListener{
            val intent = Intent(this, createUser::class.java)
            startActivity(intent)
        }

        updateDataFrameLayout.setOnClickListener{
            val intent = Intent(this, UpdateData::class.java)
            startActivity(intent)
        }

        activeLoanUsersFrameLayout.setOnClickListener{
            val intent = Intent(this, ActiveLoanUsers::class.java)
            startActivity(intent)
        }
    }
}