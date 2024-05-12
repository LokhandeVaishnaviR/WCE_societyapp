package com.example.afinal

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar

class HistoryPage : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TransactionAdapter
    private val db = FirebaseFirestore.getInstance()
    private var usersList = mutableListOf<Transaction>()
    private var originalUsersList = mutableListOf<Transaction>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history_page)

        recyclerView = findViewById(R.id.recycler_view_history)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = TransactionAdapter(this, getList())
        recyclerView.adapter = adapter


        val accountNumber = intent.getStringExtra("accountNumber")


        if (accountNumber != null) {
            fetchUsersFromFirestore(accountNumber)
        } else {
            Log.e("HistoryPage", "Account number is null")
        }
    }
    private fun getList(): List<Transaction> {
        val usersList = mutableListOf<Transaction>()
        usersList.add(Transaction(1, "12/12/12", "Special", 12000.0))
        return usersList
    }

    private fun fetchUsersFromFirestore(accountNumber: String) {
        db.collection("Users")
            .whereEqualTo("accountNo", accountNumber)
            .get()
            .addOnSuccessListener { result ->
                val transactionsList = mutableListOf<Transaction>() // List to hold all transactions
                var currentSrNo = 1 // Initial value for serial number

                for (document in result) {
                    val transactions = document.get("transactions") as? ArrayList<HashMap<String, Any>> ?: arrayListOf()

                    // Iterate over each transaction in the transactions list
                    for (transaction in transactions) {
                        val user = document.toObject(Transaction::class.java)
                        val loanType = transaction["loanType"] as String
                        user.serialNumber = currentSrNo++
                        user.loanType = loanType
                        val loanAmount = transaction["loanAmount"] as Double
                        user.loanAmount = loanAmount
                        val transactionDate = transaction["transactionDate"] as String
                        user.date = transactionDate
                        transactionsList.add(user)
                        // Create a Transaction object and add it to the transactions list

                    }

//                    transactionsList.reverse()

                    this.usersList.addAll(transactionsList)
                    originalUsersList.addAll(transactionsList)
                    adapter.setTransactions(transactionsList)
                }

                // Now, transactionsList contains all transactions from all documents in the "UserList" collection
                // You can print or display this data in your table as needed
                for (transaction in transactionsList) {
                    Log.d("TransactionData", "Serial Number: ${transaction.serialNumber}, Date: ${transaction.date}, Loan Type: ${transaction.loanType}, Loan Amount: ${transaction.loanAmount}")
                }
            }
            .addOnFailureListener { exception ->
                // Handle errors
                Log.e("FirestoreError", "Error fetching users: $exception")
            }
    }

}
