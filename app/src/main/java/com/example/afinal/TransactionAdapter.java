package com.example.afinal;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {
    Context context;
    List<Transaction> transactions;

    public TransactionAdapter(Context context, List<Transaction> transactions){
        this.context = context;
        this.transactions = transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
        notifyDataSetChanged(); // Notify RecyclerView that data has changed
    }
    @NonNull
    @Override
    public TransactionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.transaction_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionAdapter.ViewHolder holder, int position) {
        if(transactions != null && transactions.size() > 0){
            Transaction transaction = transactions.get(position);
            holder.serialNumberTextView.setText(String.valueOf(transaction.getSerialNumber()));
            holder.dateTextView.setText(transaction.getDate());
            holder.loanTypeTextView.setText(transaction.getLoanType());
            holder.loanAmountTextView.setText(String.valueOf(transaction.getLoanAmount()));

            // Set OnClickListener for any view if needed
            // For example:
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle click event here
                }
            });
        } else {
            return;
        }
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView serialNumberTextView, dateTextView, loanTypeTextView, loanAmountTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            serialNumberTextView = itemView.findViewById(R.id.serial_number_text_view);
            dateTextView = itemView.findViewById(R.id.date_text_view);
            loanTypeTextView = itemView.findViewById(R.id.loan_type_text_view);
            loanAmountTextView = itemView.findViewById(R.id.loan_amount_text_view);
        }
    }
}
