package com.example.afinal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class ActiveLoanUsers : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: UsersCount
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_active_loan_users)


        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = UsersCount(this, getList())
        recyclerView.adapter = adapter
        fetchUsersFromFirestore()
    }

    private fun getList(): List<Users> {
        val usersList = mutableListOf<Users>()
        usersList.add(Users(1, "200", "Vaishnavi Lokhande", "view"))
        return usersList
    }

    private fun fetchUsersFromFirestore() {
        db.collection("UserList")
            .whereEqualTo("societyTotalDeduction", 0) // Filter users where societyTotalDeduction is 0
            .get()
            .addOnSuccessListener { result ->
                val usersList = mutableListOf<Users>()
                var currentSrNo = 1 // Initial value for srNo
                for (document in result) {
                    val user = document.toObject(Users::class.java)
                    user.srNo = currentSrNo++
                    user.status = "view"
                    usersList.add(user)
                    Log.d("FirestoreData", "User: ${user.fullName}, Account Number: ${user.accountNumber}, Sr No: ${user.srNo}, Status: ${user.status}")
                }
                adapter.setUsers(usersList)
            }
            .addOnFailureListener { exception ->
                // Handle errors
                Log.e("FirestoreError", "Error fetching users: $exception")
            }
    }

}