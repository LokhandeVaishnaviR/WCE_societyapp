package com.example.afinal;

public class LoanUsers {
    String accountNumber;
    String fullName;
    int srNo;
    String status;

    public LoanUsers() {
        // Required empty constructor for Firestore deserialization
    }
    public LoanUsers(int srNo, String accountNumber, String fullName, String status){
        this.accountNumber = accountNumber;
        this.fullName = fullName;
        this.srNo = srNo;
        this.status = status;

    }

    public String getAccountNumber(){
        return accountNumber;
    }

    public String getFullName(){
        return fullName;
    }

    public int getSrNo(){
        return srNo;
    }

    public String getStatus(){
        return status;
    }

}

