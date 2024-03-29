package com.example.afinal

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class JamindarSelect_RequestLoan : AppCompatActivity() {


    private lateinit var editTextAccountNumber: EditText
    private lateinit var editTextNomineeName: TextInputEditText
    private lateinit var editTextAccountNumber2: EditText
    private lateinit var editTextNomineeName2: TextInputEditText
    private lateinit var buttonSubmit: Button
    private val db = FirebaseFirestore.getInstance()

    data class Surety(

        val accountNumber1: String,
        val accountNumber2: String,
        val suretyName1: String,
        val suretyName2: String,
        val personAccountNumber: String,
        val personName: String,
        val distributedAmount: Long
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jamindar_select_request_loan)

        editTextAccountNumber = findViewById(R.id.editTextAccountNumber)
        editTextNomineeName = findViewById(R.id.editTextNomineeName)
        editTextAccountNumber2 = findViewById(R.id.editTextAccountNumber2)
        editTextNomineeName2 = findViewById(R.id.editTextNomineeName2)
        buttonSubmit = findViewById(R.id.buttonSubmit)

        val accountNumber = intent.getStringExtra("accountNumber")
        val loanType = intent.getStringExtra("loanType")
        val loanValue = intent.getStringExtra("loanValue")?.toLongOrNull() ?: 0L
        val interest = intent.getStringExtra("interest")
        val installment = intent.getStringExtra("installment")
        val totalDeduction = intent.getStringExtra("totalDeduction")

        Log.d("LoanData", "Loan Type: $loanType")
        Log.d("LoanData", "Loan Value: $loanValue")
        Log.d("LoanData", "Interest: $interest")
        Log.d("LoanData", "Installment: $installment")
        Log.d("LoanData", "Total Deduction: $totalDeduction")



        editTextAccountNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                // Check if the entered account number has 3 digits
                if (s.toString().length == 3) {
                    searchAccountNumberInDatabase(s.toString(), editTextNomineeName)
                }
            }
        })

        editTextAccountNumber2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                // Check if the entered account number has 3 digits
                if (s.toString().length == 3) {
                    searchAccountNumberInDatabase(s.toString(), editTextNomineeName2)
                }
            }
        })


        val dividedAmount = loanValue/2
        buttonSubmit.setOnClickListener {
            fetchPersonName(accountNumber) { name ->
                val surety = Surety(
                    editTextAccountNumber.text.toString(),
                    editTextAccountNumber2.text.toString(),
                    editTextNomineeName.text.toString(),
                    editTextNomineeName2.text.toString(),
                    accountNumber.toString(),
                    name,
                    dividedAmount
                )
                saveSuretyData(surety)
                if (accountNumber != null) {
                    updateLoanData(accountNumber, loanType!!, loanValue, interest!!, installment!!, totalDeduction!!)
                }

            }
        }

    }

    private fun searchAccountNumberInDatabase(accountNumber: String, editTextNominee: TextInputEditText) {
        // Assuming you have a 'UserList' collection in Firestore
        db.collection("UserList")
            .whereEqualTo("accountNumber", accountNumber)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    // Account number found, populate the nominee name
                    val fullName = documents.documents[0].getString("name")
                    editTextNominee.setText(fullName)
                } else {
                    // Account number not found
                    Toast.makeText(this, "Account number not found", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                // Handle errors
                Toast.makeText(this, "Failed to search account number: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun fetchPersonName(accountNumber: String?, callback: (String) -> Unit) {
        if (accountNumber != null) {
            db.collection("UserList")
                .whereEqualTo("accountNumber", accountNumber)
                .get()
                .addOnSuccessListener { documents ->
                    if (!documents.isEmpty) {
                        val fullName = documents.documents[0].getString("fullName")
                        if (fullName != null) {
                            callback(fullName)
                        } else {
                            Toast.makeText(this, "Name not found for the account number", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this, "Account number not found", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "Failed to fetch person name: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }



    private fun saveSuretyData(surety: Surety) {
        db.collection("Surety")
            .document()
            .set(surety)
            .addOnSuccessListener {
                Toast.makeText(this, "Surety data saved successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Failed to save surety data: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }


    private fun updateLoanData(
        accountNumber: String,
        loanType: String,
        loanValue: Long,
        interest: String,
        installment: String,
        totalDeduction: String
    ) {
        // Convert interest, installment, and totalDeduction from String to Long
        val interestValue = interest.toDoubleOrNull() ?: 0.0
        val installmentValue = installment.toDoubleOrNull() ?: 0.0
        val totalDeductionValue = totalDeduction.toDoubleOrNull() ?: 0.0

        Log.d(TAG, "Interest Value: $interest")


        db.collection("UserList")
            .whereEqualTo("accountNumber", accountNumber)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val document = documents.documents[0]
                    val previousSpecialLoan = document.getLong("previousSpecialLoan") ?: 0L
                    val previousSpecialLoanInterest = document.getLong("previousSpecialLoanInterest") ?: 0L
                    val previousSpecialLoanInstallment = document.getLong("previousSpecialLoanInstallment") ?: 0L

                    val updatedPreviousSpecialLoan = previousSpecialLoan + loanValue
                    val updatedPreviousSpecialLoanInterest = previousSpecialLoanInterest + interestValue
                    val updatedPreviousSpecialLoanInstallment = previousSpecialLoanInstallment + installmentValue

                    val dataToUpdate = hashMapOf<String, Any>(
                        "previousSpecialLoan" to updatedPreviousSpecialLoan,
                        "previousSpecialLoanInterest" to updatedPreviousSpecialLoanInterest,
                        "previousSpecialLoanInstallment" to updatedPreviousSpecialLoanInstallment,
                        "totalDeduction" to (totalDeductionValue + totalDeductionValue)
                    )

                    if (loanType == "Special Loan") {
                        document.reference
                            .update(dataToUpdate)
                            .addOnSuccessListener {
                                Toast.makeText(
                                    this,
                                    "Loan data updated successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            .addOnFailureListener { exception ->
                                Toast.makeText(
                                    this,
                                    "Failed to update loan data: ${exception.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                    } else {
                        Toast.makeText(
                            this,
                            "Loan type is not Special Loan, no updates performed",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        this,
                        "Account number not found, could not update loan data",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(
                    this,
                    "Failed to fetch loan data: ${exception.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

}