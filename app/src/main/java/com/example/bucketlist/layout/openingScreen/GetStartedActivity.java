package com.example.bucketlist.layout.openingScreen;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;

import android.transition.Slide;
import android.util.Pair;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


import androidx.appcompat.app.AppCompatActivity;

import com.example.bucketlist.HomeActivity;
import com.example.bucketlist.R;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.auth.FirebaseAuth;

public class GetStartedActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    Button getstartedButton;
    ImageView ea1,ea2,ea3,ea4,logobucketlist,subSlideBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);

        mAuth = FirebaseAuth.getInstance();

        getstartedButton = findViewById(R.id.getStartedButton);
        ea1 = findViewById(R.id.ea1);
        ea2 = findViewById(R.id.ea2);
        ea3 = findViewById(R.id.ea3);
        ea4 = findViewById(R.id.ea4);
        logobucketlist = findViewById(R.id.logobucketlist);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });
    }
    //TO NEXT SLIDE FUNCTION
    public void toNextSlide(View view){
        Intent intent = new Intent(this, First_content.class);
        Slide slide = new Slide(Gravity.LEFT);
        slide.addTarget(R.id.getStartedButton);
        getWindow().setEnterTransition(slide);


        Pair[] pairs = new Pair[5];
        pairs[0]= new Pair<View,String>(ea1,"ea1transition1");
        pairs[1]= new Pair<View,String>(ea2,"ea2transition1");
        pairs[2]= new Pair<View,String>(ea3,"ea3transition1");
        pairs[3]= new Pair<View,String>(ea4,"ea4transition1");
        pairs[4]=new Pair<View,String>(logobucketlist,"logotransition1");




        ActivityOptions options =  ActivityOptions.makeSceneTransitionAnimation(this, pairs);

        startActivity(intent,options.toBundle());

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser() != null) {
            Intent i = new Intent(this, HomeActivity.class);
            finish();
            startActivity(i);
        }
    }
}
