package com.example.afinal

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class RequestLoan : AppCompatActivity() {

    private lateinit var headerTextView: MaterialTextView
    private lateinit var loanTypeSpinner: Spinner
    private lateinit var amountInputLayout: TextInputLayout

    private lateinit var interestTextView: TextView
    private lateinit var grossPayTextView: TextView
    private lateinit var interestRupeesTextView: TextView
    private lateinit var installmentRupeesTextView: TextView
    private lateinit var thirtyThreePercentTextView: TextView
    private lateinit var totalDeductionTextView: TextView
    private lateinit var loanEligibilityTextView: TextView

    private lateinit var checkEligibilityButton: Button
    private lateinit var nextButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request_loan)

        headerTextView = findViewById(R.id.text_header)
        loanTypeSpinner = findViewById(R.id.spinner_loan_type)
        amountInputLayout = findViewById(R.id.text_input_amount_layout)

        interestTextView = findViewById(R.id.interest_text)
        grossPayTextView = findViewById(R.id.gross_pay_text)
        interestRupeesTextView = findViewById(R.id.interest_rupees_text)
        installmentRupeesTextView = findViewById(R.id.installment_rupees_text)
        thirtyThreePercentTextView = findViewById(R.id.thirty_three_percent_text)
        totalDeductionTextView = findViewById(R.id.total_deduction_text)
        loanEligibilityTextView = findViewById(R.id.loan_eligibility_text)

        nextButton = findViewById(R.id.button_next)
        checkEligibilityButton = findViewById(R.id.button_check_eligibility)


        val loanSpinner: Spinner = findViewById(R.id.spinner_loan_type)

        val db = Firebase.firestore

        val accountNumber = intent.getStringExtra("accountNumber")

        ArrayAdapter.createFromResource(
            this,
            R.array.loan_options,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            loanSpinner.adapter = adapter
        }

        var loanType: Long = 500000
        var interest: Double = 8.5

        loanSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>,
                selectedItemView: View,
                position: Int,
                id: Long
            ) {
                val selectedOption = parentView.getItemAtPosition(position).toString()
                loanType = when (selectedOption) {
                    "Regular Loan" -> 500000L
                    "Special Loan" -> 2000000L
                    "Emergency Loan" -> 50000L
                    else -> 0L // Set a default value or handle this case according to your logic
                }
                interest = when(selectedOption){
                    "Regular Loan" -> 8.5
                    "Special Loan" -> 8.75
                    "Emergency Loan" -> 11.50
                    else -> 0.0
                }



                // Show a toast message indicating the selected loan type
                Toast.makeText(
                    applicationContext,
                    "$selectedOption selected",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
                // Do nothing
            }
        }

        checkEligibilityButton.setOnClickListener {
            Log.d(TAG, "Check eligibility button clicked") // Log statement to indicate button click

            val accNo = intent.getStringExtra("accountNumber")
            Log.d(TAG, "Account Number: $accNo") // Log the account number retrieved from intent

            try {
                db.collection("UserList")
                    .whereEqualTo("accountNumber", accNo)
                    .get()
                    .addOnSuccessListener { querySnapshot ->
                        for (document in querySnapshot) {
                            Log.d(TAG, "Document data: ${document.data}") // Log data retrieved from Firestore document

                            val SLI = document.getLong("previousSpecialLoanInstallment") ?: 0
                            val SLIt = document.getLong("previousSpecialLoanInterest") ?: 0
                            val ELI = document.getLong("previousEmergencyLoanInstallment") ?: 0
                            val ELIt = document.getLong("previousEmergencyLoanInterest") ?: 0
                            val CD = document.getLong("collegeDeduction") ?: 0
                            val RW = document.getLong("remainingWeeks") ?: 0
                            val prevRLI = document.getLong("previousRegularLoanInterest") ?: 0
                            val prevRLIt = document.getLong("previousRegularLoanInstallment") ?: 0
                            val Salary = document.getLong("grossPay") ?: 0

                            // Now call totalDeduct function with fetched data
                            totalDeduct(SLI, SLIt, ELI, ELIt, CD, RW, prevRLI, prevRLIt, Salary, interest, loanType)
                        }
                    }
                    .addOnFailureListener { exception ->
                        // Handle exceptions
                        Log.d(TAG, "Error getting documents: ", exception)
                        Toast.makeText(applicationContext, "Error fetching data: ${exception.message}", Toast.LENGTH_SHORT).show()
                    }
            } catch (e: Exception) {
                // Handle exceptions
                Log.e(TAG, "Error accessing Firestore: ", e)
                Toast.makeText(applicationContext, "Error accessing Firestore: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }



        nextButton.setOnClickListener {
            if (loanEligibilityTextView.text.toString().equals("Yes", ignoreCase = true)) {
                val intent = Intent(this, JamindarSelect_RequestLoan::class.java).apply {
                    putExtra("accountNumber", accountNumber)
                    putExtra("loanValue", amountInputLayout.editText?.text.toString())
                    putExtra("interest", interestRupeesTextView.text.toString())
                    putExtra("installment", installmentRupeesTextView.text.toString())
                    putExtra("totalDeduction", totalDeductionTextView.text.toString())
                    putExtra("loanType", loanSpinner.selectedItem.toString())
                }
                startActivity(intent)
            } else {
                Toast.makeText(this, "You are not eligible for the loan.", Toast.LENGTH_SHORT).show()
            }
        }
    }


    fun totalDeduct(SLI: Long, SLIt: Long, ELI: Long, ELIt: Long, CD: Long, RW: Long, prevRLI: Long, prevRLIt: Long, Salary: Long, interest: Double, loanType: Long) {

        val loanValue = amountInputLayout.editText?.text.toString().toDoubleOrNull() ?: 0.0

        // Check if loanValue is greater than 0
        if (loanValue > 0) {
            val salaryValue = Salary.toDouble()

            interestTextView.text = String.format("%.2f", interest)
            grossPayTextView.text = Salary.toString()

            val RLI = loanValue/RW
            interestRupeesTextView.text = String.format("%.2f", RLI)

            val RLIt = (loanValue * 0.0875)/12
            installmentRupeesTextView.text = String.format("%.2f", RLIt)

            val vima = 550
            val total = RLI + RLIt + ELI + ELIt + CD + vima + SLI + SLIt
            val thirtythree = salaryValue * 0.33 // based on salary
            thirtyThreePercentTextView.text = thirtythree.toString()
            totalDeductionTextView.text = String.format("%.2f", total)

            val temp = salaryValue - total
            val loanABS = temp - thirtythree

            if(loanValue > 500000){

                loanEligibilityTextView.text = "Regular Loan exceeded above limit!\nEnter amount below $loanType"
            } else {
                if(loanABS >= 0){
                    loanEligibilityTextView.text = "Yes"
                } else {
                    loanEligibilityTextView.text = "No"
                }
            }

            val maxTotal = ELI + ELIt + CD + vima + SLI + SLIt + prevRLI + prevRLIt
            // Assume minimum loan to be given is 10,000
            val interest = 10000.0 / RW.toDouble()  // Ensure RW is a numerical type
            val formattedInterest = "%.2f".format(interest)  // Formatting doesn't change data type

            val installment: Double = (10000 * 0.085) / 12.0
            val formattedInstallment : String = "%.2f".format(installment)  // Formatting doesn't change data type

            val interestTotal: Double = formattedInterest.toDouble() + formattedInstallment.toDouble() + maxTotal // Add actual numerical values

            val maxTemp: Double = salaryValue - interestTotal  // Use double for calculations
            var salaryDeduct = maxTemp - thirtythree


            val sum = formattedInterest.toDouble() + formattedInstallment.toDouble()

            salaryDeduct += sum

            // Round salaryDeduct to two decimal points
            salaryDeduct = String.format("%.2f", salaryDeduct).toDouble()

            val maxLoan = (10000 * salaryDeduct) / sum

            val formattedMaxLoan = "%.2f".format(maxLoan)
            val maxLoanNumeric = formattedMaxLoan.toDoubleOrNull() ?: 0.0
        }
    }



}