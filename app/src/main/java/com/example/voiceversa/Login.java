package com.example.voiceversa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Arrays;

public class Login extends AppCompatActivity {
    Button login;
    EditText Email,Pswd;
    public static String sharedname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login=findViewById(R.id.login);
        Email=findViewById(R.id.email_edtxt);
        Pswd=findViewById(R.id.pswd_edtxt);
        SharedPreferences preferences = getSharedPreferences("user_credentials", MODE_PRIVATE);
        final String[] username = {preferences.getString("username", null)};
        String token = preferences.getString("pswd", null);
        String finalname = Arrays.toString(username);
        sharedname = finalname.substring(1,finalname.length()-1);

        if (username[0] != null && token != null) {
            Toast.makeText(Login.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), Home_Main.class);
            startActivity(intent);
        }
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), Home_Main.class);
                startActivity(intent);
            }
        });

    }
    private String extractUsername(String email) {
        // Use substring to get the part before @
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