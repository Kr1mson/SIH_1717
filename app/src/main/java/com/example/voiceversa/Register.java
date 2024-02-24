package com.example.voiceversa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Register extends AppCompatActivity {
    Button signup;
    EditText Name, Email, Password, RePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        signup=findViewById(R.id.create_account_btn);
        Name=findViewById(R.id.name_edtxt);
        Email=findViewById(R.id.email_signup_edtxt);
        Password=findViewById(R.id.pswd_signup_edtxt);
        RePassword=findViewById(R.id.repswd_edtxt);

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