package com.example.surveyapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;
import java.util.Vector;

public class MineAdapter extends ArrayAdapter {

    List<String> survey = new Vector<>();
    Context context;
    public MineAdapter(@NonNull Context context, int resource, @NonNull List<String> survey) {
        super(context, resource, survey);
        this.survey=survey;
        this.context = context;
    }

    @Nullable
    @Override
    public String getItem(int position) {
        return survey.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.options,parent,false);
        TextView survey = convertView.findViewById(R.id.textView);
        survey.setText(getItem(position));
//        convertView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
        return convertView;
    }
}
