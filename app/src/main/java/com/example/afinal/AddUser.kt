package com.example.afinal

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddUser : AppCompatActivity() {
    private lateinit var retirementEditText: TextInputEditText
    private lateinit var totalMonthsEditText: TextInputEditText
    private lateinit var calendar: Calendar
    private lateinit var output: TextView
    private lateinit var addDataBtn: Button
    private lateinit var societyDeductionBtn: Button

    data class LoanDetails(
        val fullName: String,
        val accountNumber: String,
        val previousSpecialLoan: Double,
        val previousSpecialLoanInstallment: Double,
        val previousSpecialLoanInterest: Double,
        val previousRegularLoan:Double,
        val previousRegularLoanInstallment: Double,
        val previousRegularLoanInterest: Double,
        val previousEmergencyLoan: Double,
        val previousEmergencyLoanInstallment: Double,
        val previousEmergencyLoanInterest: Double,
        val penaltyInterest: Double,
        val remainingWeeks: Int,
        val grossPay: Double,
        val thirtyThreePercent: Double,
        val totalLoan: Double,
        val currentInstallment: Double,
        val currentInterest: Double,
        val societyTotalDeduction: Double,
        val collegeDeduction: Double,
        val totalDeduction: Double,
        val vima: Double,
        val shares: Double,
        val arrears: Double,
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_user)

        val db = Firebase.firestore
        
        val fullNameEditText = findViewById<TextInputLayout>(R.id.fullName).findViewById<TextInputEditText>(R.id.fullNameEditText)
        val AccountNoEditText = findViewById<TextInputLayout>(R.id.AccountNumber).findViewById<TextInputEditText>(R.id.AccountNumberEditText)
        val SpecialLoanEditText = findViewById<TextInputLayout>(R.id.SpecialLoan).findViewById<TextInputEditText>(R.id.SpecialLoanEditText)
        val SpecialLoanInterestEditText =  findViewById<TextInputLayout>(R.id.SpecialLoanInterest).findViewById<TextInputEditText>(R.id.SpecialLoanInterestEditText)
        val SpecialLoanInstallmentEditText = findViewById<TextInputLayout>(R.id.SpecialLoanIntstallment).findViewById<TextInputEditText>(R.id.SpecialLoanIntstallmentEditText)
        val RegularLoanEditText = findViewById<TextInputLayout>(R.id.RegularLoan).findViewById<TextInputEditText>(R.id.RegularLoanEditText)
        val RegularLoanInterestEditText =  findViewById<TextInputLayout>(R.id.RegularLoanInterest).findViewById<TextInputEditText>(R.id.RegularLoanInterestEditText)
        val RegularLoanInstallmentEditText = findViewById<TextInputLayout>(R.id.RegularLoanInstallment).findViewById<TextInputEditText>(R.id.RegularLoanInstallmentEditText)
        val EmergencyLoanEditText = findViewById<TextInputLayout>(R.id.EmergencyLoan).findViewById<TextInputEditText>(R.id.EmergencyLoanEditText)
        val EmergencyLoanInterestEditText = findViewById<TextInputLayout>(R.id.EmergencyLoanInterest).findViewById<TextInputEditText>(R.id.EmergencyLoanInterestEditText)
        val EmergencyLoanInstallmentEditText = findViewById<TextInputLayout>(R.id.EmergencyLoanInstallment).findViewById<TextInputEditText>(R.id.EmergencyLoanInstallmentEditText)
        val PenaltyInterestEditText = findViewById<TextInputLayout>(R.id.PenaltyInterest).findViewById<TextInputEditText>(R.id.PenaltyInterestEditText)
        val SharesEditText = findViewById<TextInputLayout>(R.id.Shares).findViewById<TextInputEditText>(R.id.SharesEditText)
        val GrossPayEditText =  findViewById<TextInputLayout>(R.id.GrossPay).findViewById<TextInputEditText>(R.id.GrossPayEditText)
        val ThirtyThreePercentEditText = findViewById<TextInputLayout>(R.id.ThirtyThreePercent).findViewById<TextInputEditText>(R.id.ThirtyThreePercentEditText)
        val VimaEditText = findViewById<TextInputLayout>(R.id.Vima).findViewById<TextInputEditText>(R.id.VimaEditText)
        val ArrearsEditText = findViewById<TextInputLayout>(R.id.Arrears).findViewById<TextInputEditText>(R.id.ArrearsEditText)
        val LoanEditText = findViewById<TextInputLayout>(R.id.Loan).findViewById<TextInputEditText>(R.id.LoanEditText)
        val LoanInterestEditText = findViewById<TextInputLayout>(R.id.LoanInterest).findViewById<TextInputEditText>(R.id.LoanInterestEditText)
        val LoanInstallmentEditText = findViewById<TextInputLayout>(R.id.LoanInstallment).findViewById<TextInputEditText>(R.id.LoanInstallmentEditText)
        val CollegeDeductionEditText = findViewById<TextInputLayout>(R.id.CollegeDeduction).findViewById<TextInputEditText>(R.id.CollegeDeductionEditText)
        val SocietyDeductionEditText =  findViewById<TextInputLayout>(R.id.SocietyDeduction).findViewById<TextInputEditText>(R.id.SocietyDeductionEditText)
        val TotalEditText = findViewById<TextInputLayout>(R.id.Total).findViewById<TextInputEditText>(R.id.TotalEditText)

        GrossPayEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val grossPay = s.toString().toDoubleOrNull() ?: return
                val thirtyThreePercent = String.format("%.2f", grossPay * 0.33)
                ThirtyThreePercentEditText.setText(thirtyThreePercent)
            }
        })

        retirementEditText = findViewById(R.id.retirementEditText)
        totalMonthsEditText = findViewById(R.id.totalMonthsEditText)
        calendar = Calendar.getInstance()
        addDataBtn = findViewById(R.id.addDataButton)
        societyDeductionBtn = findViewById(R.id.societyDeductionBtn)

        societyDeductionBtn.setOnClickListener {
            val specialLoanInterest = SpecialLoanInterestEditText.text.toString().toIntOrNull() ?: 0
            val specialLoanInstallment = SpecialLoanInstallmentEditText.text.toString().toIntOrNull() ?: 0
            val regularLoanInterest = RegularLoanInterestEditText.text.toString().toIntOrNull() ?: 0
            val regularLoanInstallment = RegularLoanInstallmentEditText.text.toString().toIntOrNull() ?: 0
            val emergencyLoanInterest = EmergencyLoanInterestEditText.text.toString().toIntOrNull() ?: 0
            val emergencyLoanInstallment = EmergencyLoanInstallmentEditText.text.toString().toIntOrNull() ?: 0
            val interest = LoanInterestEditText.text.toString().toIntOrNull() ?: 0
            val installment = LoanInstallmentEditText.text.toString().toIntOrNull() ?: 0
            val vima = VimaEditText.text.toString().toIntOrNull() ?: 0
            val sum = specialLoanInterest + specialLoanInstallment + regularLoanInterest + regularLoanInstallment + emergencyLoanInterest + emergencyLoanInstallment + interest + installment + vima
            val collegeDeduct = CollegeDeductionEditText.text.toString().toIntOrNull() ?: 0
            val totalSum = sum + collegeDeduct
            SocietyDeductionEditText.setText(sum.toString())
            TotalEditText.setText(totalSum.toString())
        }

        addDataBtn.setOnClickListener {

            if(fullNameEditText.text!!.isEmpty() || AccountNoEditText.text!!.isEmpty() || SpecialLoanEditText.text!!.isEmpty() || SpecialLoanInstallmentEditText.text!!.isEmpty() || SpecialLoanInterestEditText.text!!.isEmpty() || RegularLoanEditText.text!!.isEmpty() || RegularLoanInstallmentEditText.text!!.isEmpty() || RegularLoanInterestEditText.text!!.isEmpty() || EmergencyLoanEditText.text!!.isEmpty() || EmergencyLoanInstallmentEditText.text!!.isEmpty() || EmergencyLoanInterestEditText.text!!.isEmpty() || PenaltyInterestEditText.text!!.isEmpty() || GrossPayEditText.text!!.isEmpty() || ThirtyThreePercentEditText.text!!.isEmpty() || LoanEditText.text!!.isEmpty() || LoanInstallmentEditText.text!!.isEmpty() || LoanInterestEditText.text!!.isEmpty() || SocietyDeductionEditText.text!!.isEmpty() || CollegeDeductionEditText.text!!.isEmpty() || TotalEditText.text!!.isEmpty() || VimaEditText.text!!.isEmpty() || SharesEditText.text!!.isEmpty()|| ArrearsEditText.text!!.isEmpty()) {

                Toast.makeText(this,"All Fields are required!" , Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            try {
                val remainingWeeks = updateEditTexts()
                val user1 = LoanDetails(
                    fullName = fullNameEditText.text.toString(),
                    accountNumber = AccountNoEditText.text.toString(),
                    previousSpecialLoan = SpecialLoanEditText.text.toString().toDouble(),
                    previousSpecialLoanInstallment = SpecialLoanInstallmentEditText.text.toString().toDouble(),
                    previousSpecialLoanInterest = SpecialLoanInterestEditText.text.toString().toDouble(),
                    previousRegularLoan = RegularLoanEditText.text.toString().toDouble(),
                    previousRegularLoanInstallment = RegularLoanInstallmentEditText.text.toString().toDouble(),
                    previousRegularLoanInterest = RegularLoanInterestEditText.text.toString().toDouble(),
                    previousEmergencyLoan = EmergencyLoanEditText.text.toString().toDouble(),
                    previousEmergencyLoanInstallment = EmergencyLoanInstallmentEditText.text.toString().toDouble(),
                    previousEmergencyLoanInterest = EmergencyLoanInterestEditText.text.toString().toDouble(),
                    penaltyInterest = PenaltyInterestEditText.text.toString().toDouble(),
                    remainingWeeks = remainingWeeks,
                    grossPay = GrossPayEditText.text.toString().toDouble(),
                    thirtyThreePercent = ThirtyThreePercentEditText.text.toString().toDouble(),
                    totalLoan = LoanEditText.text.toString().toDouble(),
                    currentInstallment = LoanInstallmentEditText.text.toString().toDouble(),
                    currentInterest = LoanInterestEditText.text.toString().toDouble(),
                    societyTotalDeduction = SocietyDeductionEditText.text.toString().toDouble(),
                    collegeDeduction = CollegeDeductionEditText.text.toString().toDouble(),
                    totalDeduction = TotalEditText.text.toString().toDouble(),
                    vima = VimaEditText.text.toString().toDouble(),
                    shares = SharesEditText.text.toString().toDouble(),
                    arrears = ArrearsEditText.text.toString().toDouble(),
                )

                db.collection("UserList")
                    .add(user1)
                    .addOnSuccessListener { documentReference ->
                        Log.d("Info", "DocumentSnapshot added with ID: ${documentReference.id}")
                    }
                    .addOnFailureListener { e ->
                        Log.w("Info", "Error adding document", e)
                    }
                db.collection("users")
                    .get()
                    .addOnSuccessListener { result ->
                        for (document in result) {
                            Log.d("TAG", "${document.id} => ${document.data}")
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.w("TAG", "Error getting documents.", exception)
                    }


                Toast.makeText(this,user1.toString() , Toast.LENGTH_SHORT).show()
            } catch (e : Exception) {
                print(e.toString())
            }
        }

    }


    //work left - return date in string fromat and use in dataclass
    fun showDatePickerDialog(v: android.view.View) {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { view, selectedYear, selectedMonth, selectedDay ->
                calendar.set(Calendar.YEAR, selectedYear)
                calendar.set(Calendar.MONTH, selectedMonth)
                calendar.set(Calendar.DAY_OF_MONTH, selectedDay)
                updateEditTexts()
            },
            year, month, dayOfMonth
        )
        datePickerDialog.show()
    }

    private fun updateEditTexts(): Int {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formattedDate: String = sdf.format(calendar.time)
        retirementEditText.setText(formattedDate)

        val todayCalendar = Calendar.getInstance()
        val todayYear = todayCalendar.get(Calendar.YEAR)
        val todayMonth = todayCalendar.get(Calendar.MONTH)
        val yearsDifference = calendar.get(Calendar.YEAR) - todayYear
        val monthsDifference = (calendar.get(Calendar.MONTH) - todayMonth) + (yearsDifference * 12)

        totalMonthsEditText.setText("$monthsDifference months")
        return monthsDifference;
    }
}