package com.akram.mylpg;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder> {
    private final Context context;
    private final ArrayList<OrderHisModel>orderHistoryList;

    public OrderHistoryAdapter(Context context, ArrayList<OrderHisModel> orderHistoryList) {
        this.context = context;
        this.orderHistoryList = orderHistoryList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_history_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderHisModel model = orderHistoryList.get(position);

        holder.orderId.setText(String.format("Order ID: %s", model.getOrderId()));
        holder.consumerNo.setText(String.format("Consumer No: %s", model.getConsumerNo()));
        holder.distributorNo.setText(String.format("Distributor No: %s", model.getDistributorNo()));
        holder.distributorLoc.setText(String.format("Distributor Loc : %s", model.getDistLoc()));
        holder.orderTime.setText(String.format("Ordered: %s", model.getOrTime() +","+model.getOrDate()));
        holder.deliverTime.setText(String.format("Delivered: %s", model.getDeTime() +","+model.getDeDate()));
    }

    @Override
    public int getItemCount() {
        return orderHistoryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView orderId, consumerNo, distributorNo, distributorLoc, orderTime, deliverTime;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            orderId = itemView.findViewById(R.id.his_order_id);
            consumerNo = itemView.findViewById(R.id.his_consumer);
            distributorNo = itemView.findViewById(R.id.his_dist_no);
            distributorLoc = itemView.findViewById(R.id.his_dist_loc);
            orderTime = itemView.findViewById(R.id.his_order_time);
            deliverTime = itemView.findViewById(R.id.his_delivery_time);
        }
    }
}
