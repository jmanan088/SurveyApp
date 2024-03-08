package com.example.surveyapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Verify extends AppCompatActivity {

    String userid;
    FirebaseAuth auth;
    Button resend,done;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("LoginDebug","VerifyOpened ");
        setContentView(R.layout.activity_verify);
        resend = findViewById(R.id.button);
        done= findViewById(R.id.location);
        auth= FirebaseAuth.getInstance();
        userid = auth.getCurrentUser().getUid();
        FirebaseUser user = auth.getCurrentUser();
        user.reload();
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user.isEmailVerified()) {
                    //Intent i = new Intent(Verify.this, Verify.class);
                    //startActivity(i);
                    //finish();
                    userIsVerified();

                }
                else{
                    Log.d("LoginDebug","verify - User not verified");
                    user.reload().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            if(user.isEmailVerified()){
                                userIsVerified();
                            }
                            else{
                                Toast.makeText(Verify.this, "User verification failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
        if(!user.isEmailVerified()){
            resend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(Verify.this, "Please Verify the link send to your Email.", Toast.LENGTH_LONG).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("onFailure: Email Not sent",e.getMessage());
                        }
                    });
                }
            });

        }
        else{
            Log.d("LoginDebugging","Clicked me");
            userIsVerified();
        }
    }
    public void userIsVerified(){
        Intent i = new Intent(Verify.this,MainActivity.class);
        startActivity(i);
        finish();
    }
}
