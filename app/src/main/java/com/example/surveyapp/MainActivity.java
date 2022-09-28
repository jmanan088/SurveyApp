package com.example.surveyapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;
import java.util.Vector;


public class MainActivity extends AppCompatActivity {

    List <String> survey=new Vector<>();
    ListView listview;
    FirebaseAuth auth;
    Toolbar toolbar;
    String userid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        auth = FirebaseAuth.getInstance();
        userid=auth.getCurrentUser().getEmail();
        listview = findViewById(R.id.listview);
        survey.add(getString(R.string.uba));
        listview.setDivider(null);
        listview.setDividerHeight(0);
        MineAdapter ad = new MineAdapter(this,R.layout.options,survey);
        listview.setAdapter(ad);
        listview.setOnItemClickListener (new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent n = new Intent(MainActivity.this,Survey1.class);
                startActivity(n);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.user_id,menu);
        menu.findItem(R.id.user_id).setTitle(userid);
        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                auth.signOut();
                Intent i = new Intent(MainActivity.this, Login.class);
                startActivity(i);
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}