package com.akram.mylpg;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    private final Context context;
    private final ArrayList<OrderModel> orderModelArrayList;

    Calendar calendar;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy,HH:mm:ss,aaa,z");
    String dateTime;

    // Constructor
    public OrderAdapter(Context context, ArrayList<OrderModel> orderModelArrayList) {
        this.context = context;
        this.orderModelArrayList = orderModelArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // to set data to textview and imageview of each card layout
        OrderModel model = orderModelArrayList.get(position);

        DatabaseReference df = FirebaseDatabase.getInstance().getReference("Consumers/"+model.consumerNo);
        DatabaseReference dfOrder = FirebaseDatabase.getInstance().getReference("Orders/");
        df.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String currOr = snapshot.child("currOrder").getValue(String.class);
                holder.consumerNameTV.setText(snapshot.child("name").getValue(String.class));
                holder.orderStatusTV.setText(snapshot.child("address").getValue(String.class));
                holder.orderIV.setImageResource(model.image);
                String status = snapshot.child("orderStatus").getValue(String.class);
                if(status.equals("delivered")){
                    holder.shipped.setChecked(true);
                    holder.delivered.setChecked(true);
                    holder.shipped.setClickable(false);
                    holder.delivered.setClickable(false);

                }else if(status.equals("out") || status.equals("shipped")){
                    holder.shipped.setChecked(true);
                    holder.shipped.setClickable(false);
                    holder.delivered.setChecked(false);
                }else{
                    holder.shipped.setChecked(false);
                    holder.delivered.setChecked(false);
                }

                holder.shipped.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if(b)df.child("orderStatus").setValue("shipped");
                    }
                });

                holder.delivered.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if(b){
                            df.child("orderStatus").setValue("delivered");
                            calendar = Calendar.getInstance();
                            dateTime = simpleDateFormat.format(calendar.getTime()).toString();
                            dfOrder.child(currOr).child("deliveredTime").setValue(dateTime.split(",")[1]);
                            dfOrder.child(currOr).child("deliveredDate").setValue(dateTime.split(",")[0]);
                        }
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        // this method is used for showing number of card items in recycler view
        return orderModelArrayList.size();
    }

    // View holder class for initializing of your views such as TextView and Imageview
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final RelativeLayout relativeLayout;
        private final ImageView orderIV;
        private final TextView consumerNameTV;
        private final TextView orderStatusTV;
        Switch shipped,delivered;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            relativeLayout = itemView.findViewById(R.id.relativeLayout);
            orderIV = itemView.findViewById(R.id.idIVOrderImage);
            consumerNameTV = itemView.findViewById(R.id.idTVConsumerName);
            orderStatusTV = itemView.findViewById(R.id.idTVOrderStatus);
            shipped = itemView.findViewById(R.id.shipped);
            delivered = itemView.findViewById(R.id.delivered);

        }
    }
}