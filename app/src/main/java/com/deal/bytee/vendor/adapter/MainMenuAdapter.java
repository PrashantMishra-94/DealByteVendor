package com.deal.bytee.vendor.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.deal.bytee.vendor.R;
import com.deal.bytee.vendor.listnerr.menuItemClickListner;


public class MainMenuAdapter extends RecyclerView.Adapter<MainMenuAdapter.MyViewHolder> {

    Context context;
    menuItemClickListner menuItemClickListner;

    public void setMenuItemClickListner(com.deal.bytee.vendor.listnerr.menuItemClickListner menuItemClickListner) {
        this.menuItemClickListner = menuItemClickListner;
    }

    public MainMenuAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public MainMenuAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.lyt_menu, parent, false);
        return new MainMenuAdapter.MyViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull MainMenuAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuItemClickListner.menuitemclick(position);
            }
        });


    }

    @Override
    public int getItemCount() {
        return 17;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        AppCompatImageView ivBack;
        AppCompatTextView tvName;
        AppCompatTextView tvCount;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ivBack = itemView.findViewById(R.id.ivBack);
            tvName = itemView.findViewById(R.id.tvName);
            tvCount = itemView.findViewById(R.id.tvCount);
        }
    }
}
