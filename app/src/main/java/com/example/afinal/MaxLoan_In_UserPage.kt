package com.example.afinal

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MaxLoan_In_UserPage : AppCompatActivity() {

    private lateinit var spinnerLoanType: Spinner
    private lateinit var specialLoanText: TextView
    private lateinit var regularLoanText: TextView
    private lateinit var emergencyLoanText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.max_loan_changes)


        specialLoanText = findViewById(R.id.special_loan_text)
        regularLoanText = findViewById(R.id.regular_loan_text)
        emergencyLoanText = findViewById(R.id.emergency_loan_text)

//        ArrayAdapter.createFromResource(
//            this,
//            R.array.loan_options,
//            android.R.layout.simple_spinner_item
//        ).also { adapter ->
//            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//            spinnerLoanType.adapter = adapter
//        }

//        spinnerLoanType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(
//                parentView: AdapterView<*>,
//                selectedItemView: View,
//                position: Int,
//                id: Long
//            ) {
//                val selectedOption = parentView.getItemAtPosition(position).toString()
//                // Show a toast message indicating the selected loan type
//                Toast.makeText(
//                    applicationContext,
//                    "$selectedOption selected",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//
//            override fun onNothingSelected(parentView: AdapterView<*>) {
//                // Do nothing
//            }
//        }

        var loanType: Long = 500000
        var interest: Double = 8.5

        val db = Firebase.firestore

        val accNo = intent.getStringExtra("accountNumber")
        Log.d(ContentValues.TAG, "Account Number: $accNo")

        var max_special = 0.0;
        var max_regular = 0.0;
        var max_emergency = 0.0;

        try {
            db.collection("UserList")
                .whereEqualTo("accountNumber", accNo)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    for (document in querySnapshot) {
                        Log.d(ContentValues.TAG, "Document data: ${document.data}") // Log data retrieved from Firestore document

                        val SLI = document.getLong("previousSpecialLoanInstallment") ?: 0
                        val SLIt = document.getLong("previousSpecialLoanInterest") ?: 0
                        val ELI = document.getLong("previousEmergencyLoanInstallment") ?: 0
                        val ELIt = document.getLong("previousEmergencyLoanInterest") ?: 0
                        val CD = document.getLong("collegeDeduction") ?: 0
                        val RW = document.getLong("remainingWeeks") ?: 0
                        val prevRLI = document.getLong("previousRegularLoanInterest") ?: 0
                        val prevRLIt = document.getLong("previousRegularLoanInstallment") ?: 0
                        val Salary = document.getLong("grossPay") ?: 0
                        val vima = document.getLong("vima") ?: 0

                        // Now call totalDeduct function with fetched data
                        max_special = totalDeduct(SLI, SLIt, ELI, ELIt, CD, RW, prevRLI, prevRLIt, Salary, 0.087, loanType, vima)

                        max_regular = totalDeduct(SLI, SLIt, ELI, ELIt, CD, RW, prevRLI, prevRLIt, Salary, 0.085, loanType, vima)

                        max_emergency = totalDeduct(SLI, SLIt, ELI, ELIt, CD, RW, prevRLI, prevRLIt, Salary, 0.11, loanType, vima)

                        //print max special loan

                        if (max_special > 2000000) {
                            specialLoanText.text = "2500000"
                        } else {
                            specialLoanText.text = max_special.toString()
                        }

                        //print max regular loan
                        if (max_regular > 500000) {
                            regularLoanText.text = "500000"
                        } else {
                            regularLoanText.text = max_regular.toString()
                        }


                        if (max_emergency > 50000) {
                            emergencyLoanText.text = "50000"
                        } else {
                            emergencyLoanText.text = max_emergency.toString()
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    // Handle exceptions
                    Log.d(ContentValues.TAG, "Error getting documents: ", exception)
                    Toast.makeText(applicationContext, "Error fetching data: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
        } catch (e: Exception) {
            // Handle exceptions
            Log.e(ContentValues.TAG, "Error accessing Firestore: ", e)
            Toast.makeText(applicationContext, "Error accessing Firestore: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    fun totalDeduct(SLI: Long, SLIt: Long, ELI: Long, ELIt: Long, CD: Long, RW: Long, prevRLI: Long, prevRLIt: Long, Salary: Long, interest: Double, loanType: Long, vima: Long): Double {
        val salaryValue = Salary.toDouble()
        val maxTotal = ELI + ELIt + CD + vima + SLI + SLIt + prevRLI + prevRLIt
        // Assume minimum loan to be given is 10,000
        val interest = 10000.0 / RW.toDouble()  // Ensure RW is a numerical type
        val formattedInterest = "%.2f".format(interest)  // Formatting doesn't change data type
        val installment: Double = (10000 * 0.085) / 12.0
        val formattedInstallment : String = "%.2f".format(installment)  // Formatting doesn't change data type
        val interestTotal: Double = formattedInterest.toDouble() + formattedInstallment.toDouble() + maxTotal // Add actual numerical values
        val thirtythree = salaryValue * 0.33
        val maxTemp: Double = salaryValue - interestTotal  // Use double for calculations
        var salaryDeduct = maxTemp - thirtythree
        val sum = formattedInterest.toDouble() + formattedInstallment.toDouble()
        salaryDeduct += sum
        // Round salaryDeduct to two decimal points
        salaryDeduct = String.format("%.2f", salaryDeduct).toDouble()
        val maxLoan = (10000 * salaryDeduct) / sum
        val formattedMaxLoan = "%.2f".format(maxLoan)
        val maxLoanNumeric = formattedMaxLoan.toDoubleOrNull() ?: 0.0
        return maxLoanNumeric;
    }
}
