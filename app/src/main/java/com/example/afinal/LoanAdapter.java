//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//import java.util.List;
//
//public class LoanAdapter extends RecyclerView.Adapter<LoanAdapter.ViewHolder> {
//
//    private List<LoanItem> loanList;
//
//    public LoanAdapter(List<LoanItem> loanList) {
//        this.loanList = loanList;
//    }
//
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item_layout, parent, false);
//        return new ViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        LoanItem currentItem = loanList.get(position);
//        holder.textSerialNumber.setText(String.valueOf(currentItem.getSerialNumber()));
//        holder.textDate.setText(currentItem.getDate());
//        holder.textLoanType.setText(currentItem.getLoanType());
//        holder.textAmount.setText(String.valueOf(currentItem.getAmount()));
//    }
//
//    @Override
//    public int getItemCount() {
//        return loanList.size();
//    }
//
//    public static class ViewHolder extends RecyclerView.ViewHolder {
//        TextView textSerialNumber;
//        TextView textDate;
//        TextView textLoanType;
//        TextView textAmount;
//
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//            textSerialNumber = itemView.findViewById(R.id.text_serial_number);
//            textDate = itemView.findViewById(R.id.text_date);
//            textLoanType = itemView.findViewById(R.id.text_loan_type);
//            textAmount = itemView.findViewById(R.id.text_amount);
//        }
//    }
//}
