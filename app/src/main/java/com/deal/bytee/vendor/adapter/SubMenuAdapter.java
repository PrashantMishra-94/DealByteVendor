package com.deal.bytee.vendor.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.deal.bytee.vendor.R;
import com.deal.bytee.vendor.listnerr.menuEditClickListner;
import com.deal.bytee.vendor.listnerr.menuItemClickListner;
import com.deal.bytee.vendor.model.MenuModel;
import com.google.android.material.textfield.TextInputEditText;

import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@SuppressLint("SetTextI18n")
public class SubMenuAdapter extends RecyclerView.Adapter<SubMenuAdapter.MyViewHolder> {


    private Context context;
    List<MenuModel> menuModelList;
    menuItemClickListner menuItemClickListner;
    menuEditClickListner menuEditClickListner;

    public void setMenuEditClickListner(com.deal.bytee.vendor.listnerr.menuEditClickListner menuEditClickListner) {
        this.menuEditClickListner = menuEditClickListner;
    }

    public void setMenuItemClickListner(com.deal.bytee.vendor.listnerr.menuItemClickListner menuItemClickListner) {
        this.menuItemClickListner = menuItemClickListner;
    }

    public SubMenuAdapter(Context context, List<MenuModel> menuModelList) {
        this.context = context;
        this.menuModelList = menuModelList;
    }

    public SubMenuAdapter(Context context) {
        this.context = context;
    }


    @NonNull
    @Override
    public SubMenuAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.lyt_submenu, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder,
                                 @SuppressLint("RecyclerView") final int position) {


        MenuModel model = menuModelList.get(position);

        holder.tvName.setText(model.getTitle());
        holder.tvPrice.setText("Price: ₹"+model.getPrice());

        holder.tvRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuItemClickListner.menuitemclick(position);
            }
        });

        holder.tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuEditClickListner.menuitemclick(position);
            }
        });


    }


    @Override
    public int getItemCount() {
        return menuModelList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        AppCompatTextView tvName;
        AppCompatTextView tvPrice;
        AppCompatTextView tvRemove;
        AppCompatTextView tvEdit;


        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName=itemView.findViewById(R.id.tvName);
            tvPrice=itemView.findViewById(R.id.tvPrice);
            tvRemove=itemView.findViewById(R.id.tvRemove);
            tvEdit=itemView.findViewById(R.id.tvEdit);
        }
    }
}
