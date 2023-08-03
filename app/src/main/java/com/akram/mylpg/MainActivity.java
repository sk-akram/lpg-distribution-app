package com.akram.mylpg;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    //Page Contains
    //Data Inputs:: consumer no, consumer password
    // submit Button
    // distributor login Text
    EditText etConsumer, etPasswd;
    Button btn_login;
    TextView distributor_login;

    //Firebase Realtime database reference
    DatabaseReference ref;

    //login flag
    Boolean login = false;

    //EditText text:: consumer no, passwd
    String consumerNo, passwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Tapping the elements by there id
        etConsumer = findViewById(R.id.consumer_input);
        etPasswd = findViewById(R.id.password_input);
        btn_login = findViewById(R.id.login_btn);
        distributor_login = findViewById(R.id.Distributor_label);




        //setting login button on Click Listener
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //On Click
                //getting consumer no, password
                consumerNo = etConsumer.getText().toString().trim();
                passwd = etPasswd.getText().toString().trim();

                //checking if its not null
                if(consumerNo.equals("") || passwd.equals("")){
                    Toast.makeText(MainActivity.this, "Enter Credentials", Toast.LENGTH_SHORT).show();
                    return;
                }

                try{
                    //reference to the Firebase:: "Consumers/<consumerNo>
                    ref = FirebaseDatabase.getInstance().getReference("Consumers/"+consumerNo);
                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            //checking if its exists or not
                            if(snapshot.exists()){
                                //if given password matches with database then intent to consumer page
                                if(Objects.requireNonNull(snapshot.child("passwd").getValue()).toString().equals(passwd)){
                                    Intent consumerPage = new Intent(MainActivity.this, ConsumerPage.class);

                                    //Creating a bundle for msg passing
                                    Bundle consumer = new Bundle();
                                    //storing path:: consumerNo
                                    consumer.putString("path", consumerNo);
                                    consumerPage.putExtras(consumer);
                                    startActivity(consumerPage);
                                }else{
                                    Toast.makeText(MainActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                                    Toast.makeText(MainActivity.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                Toast.makeText(MainActivity.this, "User doesn't Exists", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }catch(Exception e){
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Try again", Toast.LENGTH_SHORT).show();
                }
            }
        });

        SpannableString ss = new SpannableString(distributor_login.getText());
        ClickableSpan clickableSpan1 = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Toast.makeText(MainActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, DistributorLogin.class);
                startActivity(intent);
            }

            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.BLUE);
                ds.setTextSize(30);

            }
        };

        ss.setSpan(clickableSpan1, 19, 29, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        distributor_login.setText(ss);
        distributor_login.setMovementMethod(LinkMovementMethod.getInstance());
    }
}