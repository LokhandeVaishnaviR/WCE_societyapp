package com.example.afinal

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.firestore.FirebaseFirestore

class View_AllUsers : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_all_users)


        val accountNumberTextView = findViewById<TextView>(R.id.accountNumberText)
        val fullNameEditText= findViewById<TextView>(R.id.fullNameText)

        val specialLoanEditText = findViewById<TextView>(R.id.SpecialLoanText)
        val specialLoanInterestEditText= findViewById<TextView>(R.id.SpecialLoanInterestText)
        val specialLoanInstallmentEditText = findViewById<TextView>(R.id.SpecialLoanInstallmentText)

        val regularLoanEditText = findViewById<TextView>(R.id.RegularLoanText)
        val regularLoanInterestEditText = findViewById<TextView>(R.id.RegularLoanInterestText)
        val regularLoanInstallmentEditText = findViewById<TextView>(R.id.RegularLoanInstallmentEditText)


        val emergencyLoanEditText = findViewById<TextView>(R.id.EmergencyLoanEditText)
        val emergencyLoanInterestEditText = findViewById<TextView>(R.id.EmergencyLoanInterestEditText)
        val emergencyLoanInstallmentEditText = findViewById<TextView>(R.id.EmergencyLoanInstallmentEditText)


        val penaltyInterestEditText = findViewById<TextView>(R.id.PenaltyInterestEditText)
        val sharesEditText = findViewById<TextView>(R.id.SharesEditText)
        val grossPayEditText = findViewById<TextView>(R.id.GrossPayEditText)

        val collegeDeductionEditText = findViewById<TextView>(R.id.CollegeDeductionEditText)
        val arrearsEditText = findViewById<TextView>(R.id.ArrearsEditText)
        val retirementDateEditText = findViewById<TextView>(R.id.RetirementDateEditText)

        val societyDeductionEditText = findViewById<TextView>(R.id.SocietyDeductionEditText)
        val totalEditText = findViewById<TextView>(R.id.TotalEditText)



        val accountNumber = intent.getStringExtra("accountNumber")

        if (accountNumber != null) {
            try {
                val db = FirebaseFirestore.getInstance()
                db.collection("UserList")
                    .whereEqualTo("accountNumber", accountNumber)
                    .get()
                    .addOnSuccessListener { documents ->
                        if (documents.size() > 0) {
                            val data = documents.first()

                            // Extract the required fields and display them
                            val accountNumber = data.getString("accountNumber")?.toString() ?: ""
                            val fullName = data.getString("fullName") ?: ""

                            val previousSpecialLoan = data.getLong("previousSpecialLoan") ?: 0
                            val previousSpecialLoanInstallment = data.getLong("previousSpecialLoanInstallment") ?: 0
                            val previousSpecialLoanInterest = data.getLong("previousSpecialLoanInterest") ?: 0

                            val previousRegularLoan = data.getLong("previousRegularLoan") ?: 0
                            val previousRegularLoanInstallment = data.getLong("previousRegularLoanInstallment") ?: 0
                            val previousRegularLoanInterest = data.getLong("previousRegularLoanInterest") ?: 0

                            val previousEmergencyLoan = data.getLong("previousEmergencyLoan") ?: 0
                            val previousEmergencyLoanInstallment = data.getLong("previousEmergencyLoanInstallment") ?: 0
                            val previousEmergencyLoanInterest = data.getLong("previousEmergencyLoanInterest") ?: 0

                            val penaltyInterest = data.getLong("penaltyInterest") ?: 0
                            val shares = data.getLong("shares") ?: 0
                            val grossPay = data.getLong("grossPay") ?: 0

                            val collegeDeduction = data.getLong("collegeDeduction") ?: 0
                            val arrears = data.getLong("arrears") ?: 0
                            val retirementDate = data.getLong("remainingWeeks") ?: 0

                            val societyDeduction = data.getLong("societyTotalDeduction") ?: 0
                            val total = data.getLong("totalDeduction") ?: 0




                            accountNumberTextView.text = accountNumber
                            fullNameEditText.setText(fullName)

                            specialLoanEditText.setText(previousSpecialLoan.toString())
                            specialLoanInstallmentEditText.setText(previousSpecialLoanInstallment.toString())
                            specialLoanInterestEditText.setText(previousSpecialLoanInterest.toString())

                            regularLoanEditText.setText(previousRegularLoan.toString())
                            regularLoanInstallmentEditText.setText(previousRegularLoanInstallment.toString())
                            regularLoanInterestEditText.setText(previousRegularLoanInterest.toString())

                            emergencyLoanEditText.setText(previousEmergencyLoan.toString())
                            emergencyLoanInstallmentEditText.setText(previousEmergencyLoanInstallment.toString())
                            emergencyLoanInterestEditText.setText(previousEmergencyLoanInterest.toString())

                            penaltyInterestEditText.setText(penaltyInterest.toString())
                            sharesEditText.setText(shares.toString())
                            grossPayEditText.setText(grossPay.toString())

                            collegeDeductionEditText.setText(collegeDeduction.toString())
                            arrearsEditText.setText(arrears.toString())
                            retirementDateEditText.setText(retirementDate.toString())

                            societyDeductionEditText.setText(societyDeduction.toString())
                            totalEditText.setText(total.toString())



                        } else {
                            // Handle the case where no document is found
                            specialLoanEditText.setText("Data not found!")
                        }
                    }
                    .addOnFailureListener { exception ->
                        // Handle errors
                        Log.e(TAG, "Error getting documents: $exception")
                    }
            } catch (e: Exception) {
                // Handle Firestore exceptions
                Log.e(TAG, "Firestore exception: $e")
            }
        } else {
            Log.d(TAG, "Account number is null")
            // Handle the case where the account number is null, such as displaying an error message or finishing the activity
        }

    }

    companion object {
        private const val TAG = "View_AllUsers"
    }
}
