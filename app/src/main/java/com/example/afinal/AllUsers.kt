package com.example.afinal

import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class AllUsers : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: UsersCount
    private lateinit var searchView: SearchView
    private val db = FirebaseFirestore.getInstance()
    private var usersList = mutableListOf<Users>()
    private var originalUsersList = mutableListOf<Users>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_users)

        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = UsersCount(this, getList())
        recyclerView.adapter = adapter

        searchView = findViewById(R.id.search_view)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { searchUsers(it) }
                return true
            }
        })

        fetchUsersFromFirestore()
    }


    private fun searchUsers(query: String) {
        val filteredList = originalUsersList.filter { user ->
            val fullNameMatch = user.fullName?.contains(query, ignoreCase = true) ?: false
            val accountNumberMatch = user.accountNumber?.contains(query, ignoreCase = true) ?: false
            fullNameMatch || accountNumberMatch
        }
        adapter.setUsers(filteredList)
    }



    private fun getList(): List<Users> {
        val usersList = mutableListOf<Users>()
        usersList.add(Users(1, "200", "Vaishnavi Lokhande", "view"))
        return usersList
    }

    private fun fetchUsersFromFirestore() {
        db.collection("UserList")
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
                this.usersList.addAll(usersList)
                originalUsersList.addAll(usersList)
                adapter.setUsers(usersList)
            }
            .addOnFailureListener { exception ->
                // Handle errors
                Log.e("FirestoreError", "Error fetching users: $exception")
            }
    }

}
