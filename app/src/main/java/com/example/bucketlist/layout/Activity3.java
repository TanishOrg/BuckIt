package com.example.bucketlist.layout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bucketlist.R;

public class Activity3 extends AppCompatActivity {

    TextView skipButton2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.third);

        skipButton2=(TextView) findViewById(R.id.skip2);

        skipButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(view.getContext(), loginActivity.class);
                startActivity(i);
            }
        });



    }
}
