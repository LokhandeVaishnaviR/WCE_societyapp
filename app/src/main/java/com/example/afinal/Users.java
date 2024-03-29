package com.example.afinal;

public class Users {
    String accountNumber;
    String fullName;
    int srNo;
    String status;

    public Users() {
        // Required empty constructor for Firestore deserialization
    }
    public Users(int srNo, String accountNumber, String fullName, String status){
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

