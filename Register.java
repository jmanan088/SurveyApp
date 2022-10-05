package com.example.surveyapp;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Register extends AppCompatActivity {

    private Button register;
    private EditText email,pass;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.password);
        register = findViewById(R.id.login);
        auth=FirebaseAuth.getInstance();
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail = email.getText().toString();
                String password = pass.getText().toString();
                if(TextUtils.isEmpty(mail) || TextUtils.isEmpty(password)){
                    Toast.makeText(Register.this, "Please enter the Email and Password.", Toast.LENGTH_SHORT).show();
                }
                else{
                    registration(mail,password);
                }
            }
        });
    }
    private void registration(String email, String  password){
        auth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                //Email Verification Link
                FirebaseUser user = auth.getCurrentUser();
                assert user != null;
                user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(Register.this, "Registration Successful.", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(Register.this,Verify.class);
                        startActivity(i);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("onFailure: Email Not sent",e.getMessage());
                    }
                });
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Register.this, "Registration Failed."+e, Toast.LENGTH_LONG).show();
            }
        });
    }
}