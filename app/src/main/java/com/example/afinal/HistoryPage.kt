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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history_page)

        recyclerView = findViewById(R.id.recycler_view_history)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = TransactionAdapter(this, getTransactionList()) // Provide transaction list
        recyclerView.adapter = adapter

        addSampleTransactionData()
    }

    // Method to get transaction list, you need to implement it based on your requirement
    private fun getTransactionList(): List<Transaction> {
        val transactionList = mutableListOf<Transaction>()
        // Add your logic to fetch transaction history from Firestore or any other source
        return transactionList
    }


    private fun addSampleTransactionData() {
        // Define sample transaction data
        val currentDate = Calendar.getInstance().time

        val transactionData = hashMapOf(
            "accountNumber" to "12", // Sample account number
            "loanType" to "Regular",
            "loanAmount" to 5000,
            "loanDate" to currentDate // Sample date, you can use current date or any date format you prefer
        )

        // Add the transaction data to Firestore
        try {
            db.collection("TransactionHistory").document("11")// Account number as document ID
                .collection("11") // Subcollection with same account number
                .add(transactionData) // Use add() instead of set() to let Firestore auto-generate the document ID
                .addOnSuccessListener { documentReference ->
                    // Data added successfully
                    val documentId = documentReference.id
                    val message = "Transaction added successfully with ID: $documentId"
                    showToast(message)
                    Log.d("Firestore", message) // Print to logcat
                }
                .addOnFailureListener { exception ->
                    // Handle errors
                    val errorMessage = "Failed to add transaction: $exception"
                    showToast(errorMessage)
                    Log.e("Firestore", errorMessage) // Print to logcat
                }
        } catch (e: Exception) {
            // Catch any exceptions
            val errorMessage = "Exception occurred: ${e.message}"
            showToast(errorMessage)
            Log.e("Firestore", errorMessage) // Print to logcat
        }
    }

    // Method to show toast message
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
