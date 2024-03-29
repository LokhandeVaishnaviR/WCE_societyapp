package com.example.afinal;

public class Transaction {
    int serialNumber;
    String date;
    String loanType;
    double loanAmount;

    public Transaction() {
        // Required empty constructor for Firestore deserialization
    }

    public Transaction(int serialNumber, String date, String loanType, double loanAmount){
        this.serialNumber = serialNumber;
        this.date = date;
        this.loanType = loanType;
        this.loanAmount = loanAmount;
    }

    public int getSerialNumber(){
        return serialNumber;
    }

    public String getDate(){
        return date;
    }

    public String getLoanType(){
        return loanType;
    }

    public double getLoanAmount(){
        return loanAmount;
    }
}
