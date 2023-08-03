package com.akram.mylpg;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OrderHistory extends AppCompatActivity {


    RecyclerView listHistory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
        listHistory = findViewById(R.id.historyList);

        ArrayList<OrderHisModel> orderHisModelArrayList = new ArrayList<OrderHisModel>();
        Bundle bundle = getIntent().getExtras();
        String consumerNo = bundle.getString("con");

        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Consumers/"+consumerNo+"/orders");
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot ds : snapshot.getChildren()){
                        Log.i("Element", ds.getValue(String.class));
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Orders/"+ds.getValue(String.class));
                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snap) {
                                orderHisModelArrayList.add(new OrderHisModel(snap.child("consumerNo").getValue(String.class),
                                        snap.child("distributorNo").getValue(String.class),
                                        ds.getValue(String.class),
                                        snap.child("distributorLoc").getValue(String.class),
                                        snap.child("orderTime").getValue(String.class),
                                        snap.child("orderDate").getValue(String.class),
                                        snap.child("deliveredTime").getValue(String.class),
                                        snap.child("deliveredDate").getValue(String.class)));

                                OrderHistoryAdapter orderHistoryAdapter = new OrderHistoryAdapter(OrderHistory.this, orderHisModelArrayList);
                                Log.i("Size", String.valueOf(orderHisModelArrayList.size()));
                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(OrderHistory.this, LinearLayoutManager.VERTICAL, false);
                                listHistory.setLayoutManager(linearLayoutManager);
                                listHistory.setAdapter(orderHistoryAdapter);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}