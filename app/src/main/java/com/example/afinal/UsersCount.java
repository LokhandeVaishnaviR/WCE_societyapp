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

public class UsersCount extends RecyclerView.Adapter<UsersCount.ViewHolder> {
    Context context;
    List<Users> users;

    public UsersCount(Context context, List<Users> users){
        this.context = context;
        this.users = users;
    }

    @NonNull
    @Override
    public UsersCount.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.items_table_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersCount.ViewHolder holder, int position) {
        if(users != null && users.size() > 0){
            Users user = users.get(position);
            holder.Sr_No.setText(String.valueOf(user.getSrNo()));
            holder.Account_Number.setText(user.getAccountNumber());
            holder.Name.setText(user.getFullName());
            holder.Status.setText(user.getStatus());


            // Set OnClickListener for the Status TextView
            holder.Status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Redirect to the new activity here
                    // Example:
                    Intent intent = new Intent(context, View_AllUsers.class);
                    // Pass any necessary data to the new activity
                    intent.putExtra("accountNumber", user.getAccountNumber());
                    context.startActivity(intent);
                }
            });
        } else {
            return;
        }
    }



    @Override
    public int getItemCount() {

        return users.size();
    }



    public void setUsers(List<Users> users) {
        this.users = users;
        notifyDataSetChanged(); // Notify RecyclerView about data change
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView Sr_No, Account_Number, Name, Status;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            Sr_No = itemView.findViewById(R.id.Sr_No);
            Account_Number = itemView.findViewById(R.id.Account_Number);
            Name = itemView.findViewById(R.id.Name);
            Status = itemView.findViewById(R.id.Status);
        }
    }
}