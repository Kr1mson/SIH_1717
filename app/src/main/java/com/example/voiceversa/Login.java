package com.example.voiceversa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;

public class Login extends AppCompatActivity {
    Button login;
    EditText Email,Pswd;
    public static String sharedname;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login = findViewById(R.id.login);
        Email = findViewById(R.id.email_edtxt);
        Pswd = findViewById(R.id.pswd_edtxt);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = Email.getText().toString();
                String pswd = Pswd.getText().toString();
                String uniqueUsername = extractUsername(email);
                if (email.isEmpty() || pswd.isEmpty()) {
                    Toast.makeText(Login.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                } else {
                    DatabaseReference user_ref = FirebaseDatabase.getInstance("https://voiceversa-76a7b-default-rtdb.firebaseio.com").getReference("User_Data");
                    Query checkUser = user_ref.orderByChild("username").equalTo(uniqueUsername);
                    checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                String passfromdb = snapshot.child(uniqueUsername).child("pswd").getValue(String.class);
                                String namefromdb = snapshot.child(uniqueUsername).child("name").getValue(String.class);
                                if (passfromdb.equals(pswd)) {



                                } else {
                                    Toast.makeText(Login.this, "Wrong Password/Email", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else{
                                Toast.makeText(Login.this,"User Not found please Register",Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Handle the error if the retrieval is canceled
                        }
                    });
                }
            }
        });
    }

    private String extractUsername(String email) {
        // Use substring to get the part before @gmail.com
        int atIndex = email.indexOf("@");

        if (atIndex != -1) {
            return email.substring(0, atIndex);
        } else {
            // Handle the case where the email doesn't contain @
            return email;
        }
    }

    public void onSignupClick(View view) {
        Intent i_signup=new Intent(getApplicationContext(),Register.class);
        startActivity(i_signup);
    }



}