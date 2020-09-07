package com.example.bucketlist.layout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bucketlist.R;

public class Activity2 extends AppCompatActivity {
    TextView skipButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second);


        skipButton=(TextView) findViewById(R.id.skip1);

        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(view.getContext(), Activity3.class);

                startActivity(i);
            }
        });


    }
}