package com.deal.bytee.vendor.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.deal.bytee.vendor.R;
import com.deal.bytee.vendor.model.SoldVoucherModel;

import java.util.List;


public class SellRecordsAdapter extends RecyclerView.Adapter<SellRecordsAdapter.MyViewHolder> {

    Context context;
    List<SoldVoucherModel> soldVoucherModelList;


    public SellRecordsAdapter(Context context, List<SoldVoucherModel> soldVoucherModelList) {
        this.context = context;
        this.soldVoucherModelList = soldVoucherModelList;
    }

    public SellRecordsAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public SellRecordsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.lyt_sell_record, parent, false);
        return new SellRecordsAdapter.MyViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull SellRecordsAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        SoldVoucherModel model = soldVoucherModelList.get(position);
        holder.tvUserName.setText(model.getCv_discount()+"Discount");
        holder.tvBillAmount.setText("Rs."+model.getCv_price());
        holder.tvPayAmount.setText("Rs."+model.getCv_amount_applied());
        holder.tvCode.setText(model.getCv_code());
        holder.tvStatus.setText(model.getCv_status());

    }

    @Override
    public int getItemCount() {
        return soldVoucherModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        AppCompatTextView tvUserName;
        AppCompatTextView tvCode;
        AppCompatTextView tvBillAmount;
        AppCompatTextView tvPayAmount;
        AppCompatTextView tvStatus;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvCode = itemView.findViewById(R.id.tvCode);
            tvBillAmount = itemView.findViewById(R.id.tvBillAmount);
            tvPayAmount = itemView.findViewById(R.id.tvPayAmount);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }
    }
}
