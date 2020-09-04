package com.example.bucketlist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Activity3 extends AppCompatActivity {

    Button skipButton2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.third);


        getSupportActionBar().setTitle("BucketList");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        skipButton2=(Button) findViewById(R.id.skip2);

        skipButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(view.getContext(),loginActivity.class);
                startActivity(i);
            }
        });



    }
}
