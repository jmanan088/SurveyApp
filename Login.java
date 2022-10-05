package com.example.surveyapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    private EditText email,pass;
    private Button login,register;
    private TextView forgot;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.password);
        login = findViewById(R.id.login);
        register = findViewById(R.id.registerNew);
        forgot = findViewById(R.id.forPass);
        auth = FirebaseAuth.getInstance();
//        Intent i = new Intent(Login.this,Survey1.class);
//        startActivity(i);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Login.this, Register.class);
                startActivity(i);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail = email.getText().toString();
                String password = pass.getText().toString();
                if (TextUtils.isEmpty(mail)|| TextUtils.isEmpty(password)){
                    Toast.makeText(Login.this, "Enter Email and Password.", Toast.LENGTH_SHORT).show();
                }
                else {
                    login(mail,password);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(auth.getCurrentUser()!=null){
            startActivity(new Intent(Login.this, Verify.class));
        }
    }

    private void login(String mail, String password) {
        auth.signInWithEmailAndPassword(mail, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(Login.this, "Login Successful.", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(Login.this, Verify.class);
                startActivity(i);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Login.this, "Login failed!"+ e, Toast.LENGTH_LONG).show();
            }
        });
    }


}
