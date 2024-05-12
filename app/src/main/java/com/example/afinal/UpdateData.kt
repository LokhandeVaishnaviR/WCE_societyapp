package com.example.afinal

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class UpdateData : AppCompatActivity() {

    private lateinit var retirementEditText: TextInputEditText
    private lateinit var totalMonthsEditText: TextInputEditText
    private lateinit var fullNameEditText: TextInputEditText

    private lateinit var SpecialLoanEditText: TextInputEditText
    private lateinit var SpecialLoanInterestEditText: TextInputEditText
    private lateinit var SpecialLoanInstallmentEditText: TextInputEditText

    private lateinit var RegularLoanEditText: TextInputEditText
    private lateinit var RegularLoanInterestEditText: TextInputEditText
    private lateinit var RegularLoanInstallmentEditText: TextInputEditText

    private lateinit var EmergencyLoanEditText: TextInputEditText
    private lateinit var EmergencyLoanInterestEditText: TextInputEditText
    private lateinit var EmergencyLoanInstallmentEditText: TextInputEditText

    private lateinit var PenaltyInterestEditText: TextInputEditText
    private lateinit var SharesEditText: TextInputEditText
    private lateinit var GrossPayEditText: TextInputEditText

    private lateinit var ThirtyThreePercentEditText: TextInputEditText
    private lateinit var ArrearsEditText: TextInputEditText
    private lateinit var VimaEditText: TextInputEditText

    private lateinit var CollegeDeductionEditText: TextInputEditText
    private lateinit var SocietyDeductionEditText: TextInputEditText
    private lateinit var TotalEditText: TextInputEditText

    private lateinit var SocietyDeductionBtn: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.update_data_changes)

        retirementEditText = findViewById(R.id.retirementEditText)
        totalMonthsEditText = findViewById(R.id.totalMonthsEditText)

        fullNameEditText = findViewById<TextInputLayout>(R.id.fullName).findViewById(R.id.fullNameEditText)
        SpecialLoanEditText = findViewById<TextInputLayout>(R.id.SpecialLoan).findViewById<TextInputEditText>(R.id.SpecialLoanEditText)
        SpecialLoanInterestEditText =  findViewById<TextInputLayout>(R.id.SpecialLoanInterest).findViewById<TextInputEditText>(R.id.SpecialLoanInterestEditText)
        SpecialLoanInstallmentEditText = findViewById<TextInputLayout>(R.id.SpecialLoanInstallment).findViewById<TextInputEditText>(R.id.SpecialLoanInstallmentEditText)
        RegularLoanEditText = findViewById<TextInputLayout>(R.id.RegularLoan).findViewById<TextInputEditText>(R.id.RegularLoanEditText)
        RegularLoanInterestEditText =  findViewById<TextInputLayout>(R.id.RegularLoanInterest).findViewById<TextInputEditText>(R.id.RegularLoanInterestEditText)
        RegularLoanInstallmentEditText = findViewById<TextInputLayout>(R.id.RegularLoanInstallment).findViewById<TextInputEditText>(R.id.RegularLoanInstallmentEditText)
        EmergencyLoanEditText = findViewById<TextInputLayout>(R.id.EmergencyLoan).findViewById<TextInputEditText>(R.id.EmergencyLoanEditText)
        EmergencyLoanInterestEditText = findViewById<TextInputLayout>(R.id.EmergencyLoanInterest).findViewById<TextInputEditText>(R.id.EmergencyLoanInterestEditText)
        EmergencyLoanInstallmentEditText = findViewById<TextInputLayout>(R.id.EmergencyLoanInstallment).findViewById<TextInputEditText>(R.id.EmergencyLoanInstallmentEditText)
        PenaltyInterestEditText = findViewById<TextInputLayout>(R.id.PenaltyInterest).findViewById<TextInputEditText>(R.id.PenaltyInterestEditText)
        SharesEditText = findViewById<TextInputLayout>(R.id.Shares).findViewById<TextInputEditText>(R.id.SharesEditText)
        GrossPayEditText =  findViewById<TextInputLayout>(R.id.GrossPay).findViewById<TextInputEditText>(R.id.GrossPayEditText)
        ThirtyThreePercentEditText = findViewById<TextInputLayout>(R.id.ThirtyThreePercent).findViewById<TextInputEditText>(R.id.ThirtyThreePercentEditText)
        VimaEditText = findViewById<TextInputLayout>(R.id.Vima).findViewById<TextInputEditText>(R.id.VimaEditText)
        ArrearsEditText = findViewById<TextInputLayout>(R.id.Arrears).findViewById<TextInputEditText>(R.id.ArrearsEditText)
        CollegeDeductionEditText = findViewById<TextInputLayout>(R.id.CollegeDeduction).findViewById<TextInputEditText>(R.id.CollegeDeductionEditText)
        SocietyDeductionEditText =  findViewById<TextInputLayout>(R.id.SocietyDeduction).findViewById<TextInputEditText>(R.id.SocietyDeductionEditText)
        TotalEditText = findViewById<TextInputLayout>(R.id.Total).findViewById<TextInputEditText>(R.id.TotalEditText)

        SocietyDeductionBtn = findViewById(R.id.societyDeductionBtn)

        //calculates total and society deduction
        SocietyDeductionBtn.setOnClickListener {

            val specialLoanInterest = SpecialLoanInterestEditText.text.toString().toDoubleOrNull() ?: 0.0
            val specialLoanInstallment = SpecialLoanInstallmentEditText.text.toString().toDoubleOrNull() ?: 0.0
            val regularLoanInterest = RegularLoanInterestEditText.text.toString().toDoubleOrNull() ?: 0.0
            val regularLoanInstallment = RegularLoanInstallmentEditText.text.toString().toDoubleOrNull() ?: 0.0
            val emergencyLoanInterest = EmergencyLoanInterestEditText.text.toString().toDoubleOrNull() ?: 0.0
            val emergencyLoanInstallment = EmergencyLoanInstallmentEditText.text.toString().toDoubleOrNull() ?: 0.0
            val vima = VimaEditText.text.toString().toDoubleOrNull() ?: 0.0

            val sum = specialLoanInterest + specialLoanInstallment + regularLoanInterest + regularLoanInstallment + emergencyLoanInterest + emergencyLoanInstallment + vima

            val collegeDeduct = CollegeDeductionEditText.text.toString().toDoubleOrNull() ?: 0.0
            val totalSum = sum + collegeDeduct

            SocietyDeductionEditText.setText(sum.toString())
            TotalEditText.setText(totalSum.toString())

        }


        GrossPayEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val grossPay = s.toString().toDoubleOrNull() ?: return
                val thirtyThreePercent = String.format("%.2f", grossPay * 0.33)
                ThirtyThreePercentEditText.setText(thirtyThreePercent)
            }
        })




        retirementEditText.setOnClickListener {
            showDatePickerDialog()
        }


//        TO FETCH AND DISPLAY DATA
        val accountNumberEditText = findViewById<TextInputLayout>(R.id.accountNumberLayout).findViewById<TextInputEditText>(R.id.accountNumberEditText)
        val fetchButton = findViewById<Button>(R.id.fetchDataButton)

        fetchButton.setOnClickListener {
            val accountNumber = accountNumberEditText.text.toString()
            if (accountNumber.isNotEmpty()) {
                try {
                    val db = Firebase.firestore
                    db.collection("UserList")
                        .whereEqualTo("accountNumber", accountNumber).get()
                        .addOnSuccessListener { documents ->
                            if (documents.size() > 0) {
                                val data = documents.first().data // Retrieve the document data
                                displayData(data)
                            } else {
                                // Document does not exist
                                Toast.makeText(this, "Account number not found", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }

                } catch (e: Exception) {
                    println("Firestore exception: $e")
                }
            }
        }


        // Create a new button or event handler for updating database
        // Create a new button or event handler for updating database
        val updateButton = findViewById<Button>(R.id.updateDataButton)
        updateButton.setOnClickListener {
            // Your code to retrieve data and update the database
            val accountNumber = accountNumberEditText.text.toString()
            if (accountNumber.isNotEmpty()) {
                try {
                    val db = Firebase.firestore
                    db.collection("UserList")
                        .whereEqualTo("accountNumber", accountNumber).get()
                        .addOnSuccessListener { documents ->
                            if (documents.size() > 0) {
                                val documentId = documents.first().id
                                val newData = mapOf<String, Any>(
                                    "fullName" to fullNameEditText.text.toString(),
                                    "previousSpecialLoan" to (SpecialLoanEditText.text.toString().toDoubleOrNull() ?: 0.0),
                                    "previousSpecialLoanInterest" to (SpecialLoanInterestEditText.text.toString().toDoubleOrNull() ?: 0.0),
                                    "previousSpecialLoanInstallment" to (SpecialLoanInstallmentEditText.text.toString().toDoubleOrNull() ?: 0.0),
                                    "previousRegularLoan" to (RegularLoanEditText.text.toString().toDoubleOrNull() ?: 0.0),
                                    "previousRegularLoanInterest" to (RegularLoanInterestEditText.text.toString().toDoubleOrNull() ?: 0.0),
                                    "previousRegularLoanInstallment" to (RegularLoanInstallmentEditText.text.toString().toDoubleOrNull() ?: 0.0),
                                    "previousEmergencyLoan" to (EmergencyLoanEditText.text.toString().toDoubleOrNull() ?: 0.0),
                                    "previousEmergencyLoanInterest" to (EmergencyLoanInterestEditText.text.toString().toDoubleOrNull() ?: 0.0),
                                    "previousEmergencyLoanInstallment" to (EmergencyLoanInstallmentEditText.text.toString().toDoubleOrNull() ?: 0.0),
                                    "penaltyInterest" to (PenaltyInterestEditText.text.toString().toDoubleOrNull() ?: 0.0),
                                    "shares" to (SharesEditText.text.toString().toDoubleOrNull() ?: 0.0),
                                    "grossPay" to (GrossPayEditText.text.toString().toDoubleOrNull() ?: 0.0),
                                    "thirtyThreePercent" to (ThirtyThreePercentEditText.text.toString().toDoubleOrNull() ?: 0.0),
                                    "remainingWeeks" to (totalMonthsEditText.text.toString().toDoubleOrNull() ?: 0.0),
                                    "vima" to (VimaEditText.text.toString().toDoubleOrNull() ?: 0.0),
                                    "arrears" to (ArrearsEditText.text.toString().toDoubleOrNull() ?: 0.0),
                                    "societyTotalDeduction" to (SocietyDeductionEditText.text.toString().toDoubleOrNull() ?: 0.0),
                                    "collegeDeduction" to (CollegeDeductionEditText.text.toString().toDoubleOrNull() ?: 0.0),
                                    "totalDeduction" to (TotalEditText.text.toString().toDoubleOrNull() ?: 0.0)

                                )


                                db.collection("UserList").document(documentId)
                                    .update(newData)
                                    .addOnSuccessListener {
                                        Toast.makeText(this, "Data updated successfully", Toast.LENGTH_SHORT).show()
                                    }
                                    .addOnFailureListener { e ->
                                        Toast.makeText(this, "Error updating data: $e", Toast.LENGTH_SHORT).show()
                                    }
                            } else {
                                // Document does not exist
                                Toast.makeText(this, "Account number not found", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }

                } catch (e: Exception) {
                    println("Firestore exception: $e")
                }
            }
        }




    }



    //    Display Data on Fetch instruction
    private fun displayData(data: Map<String, Any>?) {
        data?.let {
            fullNameEditText.setText(it["fullName"].toString())

            SpecialLoanEditText.setText(it["previousSpecialLoan"].toString())
            SpecialLoanInterestEditText.setText(it["previousSpecialLoanInterest"].toString())
            SpecialLoanInstallmentEditText.setText(it["previousSpecialLoanInstallment"].toString())

            RegularLoanEditText.setText(it["previousRegularLoan"].toString())
            RegularLoanInterestEditText.setText(it["previousRegularLoanInterest"].toString())
            RegularLoanInstallmentEditText.setText(it["previousRegularLoanInstallment"].toString())

            EmergencyLoanEditText.setText(it["previousEmergencyLoan"].toString())
            EmergencyLoanInterestEditText.setText(it["previousEmergencyLoanInterest"].toString())
            EmergencyLoanInstallmentEditText.setText(it["previousEmergencyLoanInstallment"].toString())

            PenaltyInterestEditText.setText(it["penaltyInterest"].toString())
            SharesEditText.setText(it["shares"].toString())
            GrossPayEditText.setText(it["grossPay"].toString())

            ThirtyThreePercentEditText.setText(it["previousEmergencyLoan"].toString())
            VimaEditText.setText(it["vima"].toString())
            ArrearsEditText.setText(it["arrears"].toString())

            SocietyDeductionEditText.setText(it["societyTotalDeduction"].toString())
            CollegeDeductionEditText.setText(it["collegeDeduction"].toString())
            TotalEditText.setText(it["totalDeduction"].toString())
        }
    }


    //function for date picker and calculates month

    fun showDatePickerDialog() {
        val datePicker = DatePickerDialog(this)
        datePicker.setOnDateSetListener { view, year, month, dayOfMonth ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(Calendar.YEAR, year)
            selectedDate.set(Calendar.MONTH, month)
            selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val currentDate = Calendar.getInstance()

            val monthsDifference =
                calculateMonthsDifference(currentDate.time, selectedDate.time)
            totalMonthsEditText.setText(monthsDifference.toString())
        }
        datePicker.show()
    }

    fun calculateMonthsDifference(startDate: Date, endDate: Date): Int {
        val startCalendar = Calendar.getInstance()
        val endCalendar = Calendar.getInstance()

        startCalendar.time = startDate
        endCalendar.time = endDate

        val diffYear = endCalendar[Calendar.YEAR] - startCalendar[Calendar.YEAR]
        val diffMonth = endCalendar[Calendar.MONTH] - startCalendar[Calendar.MONTH]

        return diffYear * 12 + diffMonth
    }
}
