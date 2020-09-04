package com.example.bucketlist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Activity2 extends AppCompatActivity { ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second);

        getSupportActionBar().setTitle("BucketList");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);







    }
}