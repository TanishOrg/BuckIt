package com.example.bucketlist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Activity2 extends AppCompatActivity {
    Button skipButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second);

        getSupportActionBar().setTitle("BucketList");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        skipButton=(Button) findViewById(R.id.skip1);

        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(view.getContext(),Activity3.class);

                startActivity(i);
            }
        });


    }
}