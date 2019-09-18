package com.example.tongan.myapplication.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tongan.myapplication.Classes.Order;
import com.example.tongan.myapplication.R;

import java.util.ArrayList;

public class OrdersRecyclerViewAdapter extends RecyclerView.Adapter<OrdersRecyclerViewAdapter.ViewHolder> {

    public static final String TAG = "OrdersRecyclerViewAdapter";

    private Context context;
    private ArrayList<Order> ordersAL;

    public OrdersRecyclerViewAdapter(Context context, ArrayList<Order> ordersAL) {
        this.context = context;
        this.ordersAL = ordersAL;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_recycler_view_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return ordersAL.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView profileImage;
        TextView serviceDisplayName;
        ImageView serviceImage;
        TextView serviceTitle;
        TextView serviceDescription;
        TextView servicePrice;
        Button chatBtn;
        Button cancelBtn;
        Button payNowBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            profileImage = itemView.findViewById(R.id.profileImage);
            serviceDisplayName = itemView.findViewById(R.id.serviceDisplayName);
            serviceImage = itemView.findViewById(R.id.serviceImage);
            serviceTitle = itemView.findViewById(R.id.serviceTitle);
            serviceDescription = itemView.findViewById(R.id.serviceDescription);
            servicePrice = itemView.findViewById(R.id.servicePrice);
            chatBtn = itemView.findViewById(R.id.chatBtn);
            cancelBtn = itemView.findViewById(R.id.cancelBtn);
            payNowBtn = itemView.findViewById(R.id.payNowBtn);
        }
    }

}
