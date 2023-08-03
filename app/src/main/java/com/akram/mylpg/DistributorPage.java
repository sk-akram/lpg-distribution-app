package com.akram.mylpg;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class DistributorPage extends AppCompatActivity {
    TextView disName_txt, disID_txt, disLoc_txt;
    Toolbar toolbar;
    RecyclerView orderRV;

    String name, id, loc;

//    String orderName, orderStatus, orderId, orderConsumerNo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distributor_page);

        orderRV = findViewById(R.id.idRVOrder);
        disName_txt = findViewById(R.id.distributorName);
        disID_txt = findViewById(R.id.distributorId);
        disLoc_txt = findViewById(R.id.distributorLoc);

        toolbar = findViewById(R.id.toolbar2);
        Bundle bundle = getIntent().getExtras();
        String distributorPath = bundle.getString("path");
        ArrayList<OrderModel> orderModelArrayList = new ArrayList<OrderModel>();


        try{

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(distributorPath);
            ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                name = Objects.requireNonNull(snapshot.child("name").getValue()).toString();
                id = Objects.requireNonNull(snapshot.child("id").getValue()).toString();
                loc =Objects.requireNonNull(snapshot.child("loc").getValue()).toString();


                disName_txt.setText("Name: ".concat(name));
                toolbar.setTitle("Welcome, "+ name.split(" ")[0]);
                disID_txt.setText("ID: ".concat(id));
                disLoc_txt.setText("Location: ".concat(loc));

                for(DataSnapshot snap: snapshot.child("Consumers").getChildren()){
                    OrderModel ob = new OrderModel(snap.getKey(),R.drawable.gas_quota, snap.getValue(String.class));
                    orderModelArrayList.add(ob);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        }catch(Exception e){
            e.printStackTrace();
        }

        setSupportActionBar(toolbar);


        OrderAdapter courseAdapter = new OrderAdapter(DistributorPage.this, orderModelArrayList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DistributorPage.this, LinearLayoutManager.VERTICAL, false);
        orderRV.setLayoutManager(linearLayoutManager);
        orderRV.setAdapter(courseAdapter);
    }
}