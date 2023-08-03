package com.akram.mylpg;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TrackOrder extends AppCompatActivity {
    Toolbar toolbar;
    ProgressBar progressBar;
    TextView txtTrack,txtOrderedTime, txtDeliveredTime;
    Button orderHistoryBtn;

    int p = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_order);

        progressBar=findViewById(R.id.progressBar);
        txtTrack = findViewById(R.id.trackId);
        txtOrderedTime = findViewById(R.id.orderedTime);
        txtDeliveredTime = findViewById(R.id.deliveredTime);
        toolbar = findViewById(R.id.toolbar);
        orderHistoryBtn = findViewById(R.id.orderHistoryBtn);


        Bundle bundle = getIntent().getExtras();
        String consumerNo = bundle.getString("stuff3");

        toolbar.setTitle("Consumer Id: "+consumerNo);
        setSupportActionBar(toolbar);
        txtOrderedTime.setText("Order Placed! \n Order will be accepted soon");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Consumers/"+consumerNo);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                    String order = snapshot.child("orderStatus").getValue(String.class);
                    String trackPath = snapshot.child("currOrder").getValue(String.class);
                    if(order.equals("delivered")){
                        p = 100;
                    }else if(order.equals("out")){
                        p = 66;
                    }else if(order.equals("shipped")){
                        p = 33;
                    }
                    progressBar.setProgress(p);

                    DatabaseReference refOrder = FirebaseDatabase.getInstance().getReference("Orders");
                    refOrder.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snap) {
                            assert trackPath != null;
                            String st = "Ordered Time"+"\n"+snap.child(trackPath).child("orderTime").getValue(String.class) + "\n" + snap.child(trackPath).child("orderDate").getValue(String.class);

                            if(p!=100 && p!=1){
                                txtOrderedTime.setText(st);
                                txtDeliveredTime.setVisibility(View.INVISIBLE);
                            }
                            if(p==100){
                                String st2 = "Delivered Time"+"\n"+snap.child(trackPath).child("deliveredTime").getValue(String.class) + "\n" + snap.child(trackPath).child("deliveredDate").getValue(String.class);
                                txtDeliveredTime.setText(st2);
                                txtOrderedTime.setText(st);
                                txtDeliveredTime.setVisibility(View.VISIBLE);

                            }
                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        orderHistoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent orderHis = new Intent(TrackOrder.this, OrderHistory.class);
                Bundle pat = new Bundle();
                pat.putString("con", consumerNo);
                orderHis.putExtras(pat);
                startActivity(orderHis);
            }
        });
    }
}