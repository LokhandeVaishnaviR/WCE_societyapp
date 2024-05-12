package com.example.afinal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import androidx.cardview.widget.CardView

class Loan_In_UserPage : AppCompatActivity() {

    private lateinit var requestLoanFrameLayout: CardView
    private lateinit var maxLoanFrameLayout: CardView
    private lateinit var deductionDataFrameLayout: CardView
    private lateinit var loanDetailsUsersFrameLayout: CardView
    private lateinit var suretyFrameLayout: CardView
    private lateinit var loanHistory: CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.users_operations)

        requestLoanFrameLayout = findViewById(R.id.request_loan)
        maxLoanFrameLayout = findViewById(R.id.max_loan)
        deductionDataFrameLayout = findViewById(R.id.deduction_details)
        loanDetailsUsersFrameLayout = findViewById(R.id.loan_details)
        suretyFrameLayout = findViewById(R.id.surety)
        loanHistory = findViewById(R.id.loan_history)


        val accountNumber = intent.getStringExtra("accountNumber")

        requestLoanFrameLayout.setOnClickListener {
            val intent = Intent(this, RequestLoan::class.java).apply {
                putExtra("accountNumber", accountNumber)
            }
            startActivity(intent)
        }

        maxLoanFrameLayout.setOnClickListener{
            val intent = Intent(this, MaxLoan_In_UserPage::class.java).apply {
                putExtra("accountNumber", accountNumber)
            }
            startActivity(intent)
        }

        suretyFrameLayout.setOnClickListener{
            val intent = Intent(this, SuretyPage::class.java).apply {
                putExtra("accountNumber", accountNumber)
            }
            startActivity(intent)
        }

        loanHistory.setOnClickListener{
            val intent = Intent(this, HistoryPage::class.java).apply {
                putExtra("accountNumber", accountNumber)
            }
            startActivity(intent)
        }

    }
}