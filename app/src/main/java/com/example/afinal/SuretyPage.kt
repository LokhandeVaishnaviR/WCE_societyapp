package com.example.afinal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.SearchView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class SuretyPage : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: UsersCount
    private var usersList = mutableListOf<Users>()
    private var originalUsersList = mutableListOf<Users>()
    private lateinit var searchView: SearchView


    // Initialize Firestore instance
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_surety_page)

        // Get account number from intent
        val accountNumber = intent.getStringExtra("accountNumber")


        searchView = findViewById(R.id.search_view)

        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = UsersCount(this, usersList)
        recyclerView.adapter = adapter

        // Call function to fetch user data from Firestore
        fetchUsersFromFirestore(accountNumber)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { searchUsers(it) }
                return true
            }
        })
    }

    private fun searchUsers(query: String) {
        val filteredList = originalUsersList.filter { user ->
            user.fullName.contains(query, ignoreCase = true) || user.accountNumber.contains(query, ignoreCase = true)
        }
        adapter.setUsers(filteredList)
    }


    private fun fetchUsersFromFirestore(accountNumber: String?) {
        if (accountNumber != null) {
            val usersList = mutableListOf<Users>()
            var totalAmount = 0.0
            var currentSrNo = 1 // Initial value for srNo

            // Query Firestore to find documents where accountNumber matches either accountNumber1 or accountNumber2
            db.collection("Surety")
                .whereEqualTo("accountNumber1", accountNumber)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val personName = document.getString("personName") ?: ""
                        val personAccountNumber = document.getString("personAccountNumber") ?: ""
                        val distributedAmount = document.getString("distributedAmount") ?: ""



                        val halfDistributedAmount = (distributedAmount.toDoubleOrNull() ?: 0.0) / 2
                        val formattedHalfDistributedAmount = String.format("%.2f", halfDistributedAmount)

                        totalAmount += halfDistributedAmount

                        // Create a new Users object and add it to the list
                        val user = Users(currentSrNo++, personAccountNumber, personName, formattedHalfDistributedAmount)
                        usersList.add(user)
                    }

                    originalUsersList.addAll(usersList)
                    // After fetching from accountNumber1, check if any results are found
                    // If not found, proceed to check accountNumber2
                    if (documents.isEmpty) {
                        db.collection("Surety")
                            .whereEqualTo("accountNumber2", accountNumber)
                            .get()
                            .addOnSuccessListener { documents2 ->
                                for (document2 in documents2) {
                                    val personName = document2.getString("personName") ?: ""
                                    val personAccountNumber = document2.getString("personAccountNumber") ?: ""
                                    val distributedAmount = document2.getString("distributedAmount") ?: ""

                                    // Create a new Users object and add it to the list

                                    val halfDistributedAmount = (distributedAmount.toDoubleOrNull() ?: 0.0) / 2
                                    val formattedHalfDistributedAmount = String.format("%.2f", halfDistributedAmount)

                                    totalAmount += halfDistributedAmount

                                    val user = Users(currentSrNo++, personAccountNumber, personName, formattedHalfDistributedAmount)
                                    usersList.add(user)
                                }
                                usersList.add(Users(0, "", "Total", totalAmount.toString()))
                                adapter.setUsers(usersList)
                            }
                            .addOnFailureListener { exception ->
                                // Handle errors
                                Log.e("FirestoreError", "Error fetching users: $exception")
                            }
                    } else {
                        adapter.setUsers(usersList)
                    }
                }
                .addOnFailureListener { exception ->
                    // Handle errors
                    Log.e("FirestoreError", "Error fetching users: $exception")
                }
        }
    }
}