package com.akram.mylpg;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DistributorLogin extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    EditText distributorId, passwd;
    Button btn_sign;
    TextView forgetPasswd;
    DatabaseReference referenceDist;
    Spinner spinDis;

    String[] distLoc = { "Select Centre","Durgapur LPG", "Begampur LPG",
            "Kolkata LPG", "Bardhamaan LPG",
            "Dankuni LPG", "New Delhi LPG", "Mumbai LPG" };
    ArrayAdapter aa;
    String dist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distributor_login);

        distributorId = findViewById(R.id.distributor_input);
        passwd = findViewById(R.id.password_input);
        btn_sign = findViewById(R.id.login_btn);
        forgetPasswd = findViewById(R.id.forget_password_label);

        spinDis = findViewById(R.id.disSpinner);
        spinDis.setOnItemSelectedListener(this);


        aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,distLoc);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinDis.setAdapter(aa);

        btn_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String path = "Distributors/"+dist+"//"+distributorId.getText().toString().trim();
                    referenceDist = FirebaseDatabase.getInstance().getReference().child(path);
                    referenceDist.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                if(snapshot.child("passwd").getValue().toString().equals(passwd.getText().toString().trim())){
                                    Intent distributorPage = new Intent(DistributorLogin.this, DistributorPage.class);
                                    Bundle distr = new Bundle();
                                    distr.putString("path", path);
                                    distributorPage.putExtras(distr);
                                    startActivity(distributorPage);
                                }
                                else{
                                    Toast.makeText(DistributorLogin.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                Toast.makeText(DistributorLogin.this, "Doesn't Exit", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        });



    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if(i!=0){
            dist = distLoc[i];
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}