package com.akram.mylpg;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class ConsumerPage extends AppCompatActivity{

    //Consumer ID
    Toolbar toolbar;

    //Consumer name, address, distributor, city
    TextView txtName, txtAddress, txtDistributor, txtCity;
    //Button:: Order, track order
    Button btnOrder,btnTrackOrder;

    int quota,id;
    ImageView[] gas = new ImageView[12];
    Order order;
    String dateTime;
    Calendar calendar;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy, HH:mm:ss, aaa, z");
    NewOrder newOrder = new NewOrder();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumer_page);

        txtName = findViewById(R.id.consumerName);
        txtDistributor = findViewById(R.id.distributorName);
        txtCity = findViewById(R.id.consumerCity);
        txtAddress = findViewById(R.id.consumerAddress);
        btnTrackOrder = findViewById(R.id.trackOrder);

        toolbar = findViewById(R.id.toolbar);
        Bundle bundle = getIntent().getExtras();
        String consumerNo = bundle.getString("path");
        toolbar.setTitle("Consumer Id: "+consumerNo);
        setSupportActionBar(toolbar);

        final String[] ot = new String[1];
        final String[] od = new String[1];
        gas[0] = findViewById(R.id.gas1);
        gas[1] = findViewById(R.id.gas2);
        gas[2] = findViewById(R.id.gas3);
        gas[3] = findViewById(R.id.gas4);
        gas[4] = findViewById(R.id.gas5);
        gas[5] = findViewById(R.id.gas6);
        gas[6] = findViewById(R.id.gas7);
        gas[7] = findViewById(R.id.gas8);
        gas[8] = findViewById(R.id.gas9);
        gas[9] = findViewById(R.id.gas10);
        gas[10] = findViewById(R.id.gas11);
        gas[11] = findViewById(R.id.gas12);
        btnOrder = findViewById(R.id.order);


        try {
            DatabaseReference refCheck = FirebaseDatabase.getInstance().getReference().child("Consumers/" + consumerNo);
            refCheck.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        txtName.setText(String.format("Name: %s", Objects.requireNonNull(snapshot.child("name").getValue()).toString()));

                        txtAddress.setText(String.format("Address: %s", Objects.requireNonNull(snapshot.child("address").getValue()).toString()));

                        txtCity.setText(String.format("City: %s", Objects.requireNonNull(snapshot.child("city").getValue()).toString()));

                        txtDistributor.setText(String.format("Distributor: %s", Objects.requireNonNull(snapshot.child("distributor").getValue()).toString()));

                        quota = Integer.parseInt(Objects.requireNonNull(snapshot.child("quota").getValue()).toString());
                        for(int i=11;i>=quota;i--){
                            gas[i].setVisibility(View.INVISIBLE);
                        }

                        String orderS = snapshot.child("orderStatus").getValue(String.class);

                        if(quota==0 || (!orderS.equals("") && !orderS.equals("delivered"))){
                            btnOrder.setBackgroundColor(Color.RED);
                        }else {
                            btnOrder.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if(orderS.equals("") || orderS.equals("delivered")){
                                        String prev = snapshot.child("currOrder").getValue(String.class);
                                        refCheck.child("prevOrder").setValue(prev);

                                        if(quota!=0){
                                            quota = quota - 1;
                                            String dist = Objects.requireNonNull(snapshot.child("distributor").getValue()).toString();
                                            String trackPath = consumerNo+dist +String.valueOf(quota);
                                            //refCheck.child("orders").child(String.valueOf(quota+1)).setValue(trackPath);
                                            refCheck.child("currOrder").setValue(trackPath);
                                            refCheck.child("orderStatus").setValue("ordered");


                                            order = new Order();
                                            order.setConsumerNo(consumerNo);
                                            order.setDistributorNo(snapshot.child("distPath").getValue(String.class));
                                            order.setStatus("ordered");
                                            //refOrder.setValue(order);
                                            DatabaseReference refDist = FirebaseDatabase.getInstance().getReference().child("Distributors/"+dist+"//"+order.getDistributorNo()+"//Consumers");
                                            refDist.child(consumerNo).setValue(trackPath);

                                            refCheck.child("quota").setValue(String.valueOf(quota));
                                            for (int i = 11; i >= quota; i--) {
                                                gas[i].setVisibility(View.INVISIBLE);
                                            }
                                            if(orderS.equals("delivered")){
                                                refCheck.child("orders").child(String.valueOf(quota)).setValue(prev);
                                            }

                                            DatabaseReference refOrder = FirebaseDatabase.getInstance().getReference().child("Orders/"+trackPath);
                                            calendar = Calendar.getInstance();
                                            dateTime = simpleDateFormat.format(calendar.getTime());
                                            newOrder.setConsumerNo(consumerNo);
                                            newOrder.setDistributorNo(order.getDistributorNo());
                                            newOrder.setOrderTime(dateTime.split(",")[1]);
                                            newOrder.setOrderDate(dateTime.split(",")[0]);
                                            newOrder.setDeliveredDate("");
                                            newOrder.setDeliveredTime("");
                                            newOrder.setDistributorLoc(dist);
                                            refOrder.setValue(newOrder);

                                        }
                                    }else{
                                        btnOrder.setBackgroundColor(Color.RED);
                                    }
                                }
                            });
                        }
                    }
                }


                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
        btnTrackOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(ConsumerPage.this, TrackOrder.class);
                Bundle consumerPath = new Bundle();
                consumerPath.putString("stuff3", consumerNo);

                it.putExtras(consumerPath);
                startActivity(it);
            }
        });

    }
}