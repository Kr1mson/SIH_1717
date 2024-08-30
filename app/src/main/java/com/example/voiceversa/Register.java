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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {
    Button signup;
    EditText Name, Email, Password, RePassword;

    FirebaseDatabase userdb;
    DatabaseReference user_ref;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        signup=findViewById(R.id.create_account_btn);
        Name=findViewById(R.id.name_edtxt);
        Email=findViewById(R.id.email_signup_edtxt);
        Password=findViewById(R.id.pswd_signup_edtxt);
        RePassword=findViewById(R.id.repswd_edtxt);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=Name.getText().toString();
                String email=Email.getText().toString();
                String pswd=Password.getText().toString();
                String repswd=RePassword.getText().toString();
                String uniqueUsername = extractUsername(email);
                if(name.equals("")||email.equals("")||pswd.equals("")) {
                    Toast.makeText(Register.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                }
                else {
                    if(pswd.length()>=8) {
                        if (pswd.equals(repswd)) {
                            User_Helper uh = new User_Helper(name, email, pswd,uniqueUsername);
                            userdb = FirebaseDatabase.getInstance("https://voiceversa-76a7b-default-rtdb.firebaseio.com");
                            user_ref = userdb.getReference("User_Data");
                            user_ref.child(uniqueUsername).setValue(uh).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Name.setText("");
                                    Email.setText("");
                                    Password.setText("");
                                    RePassword.setText("");


                                }
                            });
                        } else {
                            Toast.makeText(Register.this, "Passwords doesn't match", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(Register.this,"Password Must be atleast 8 characters long",Toast.LENGTH_SHORT).show();
                    }
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
    public void onLoginClick(View view) {
        Intent i_signup=new Intent(getApplicationContext(),Login.class);
        startActivity(i_signup);
    }
}