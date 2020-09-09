package com.example.bucketlist;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;

import android.transition.Slide;
import android.util.Pair;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;


import androidx.appcompat.app.AppCompatActivity;

import com.example.bucketlist.First_content;
import com.example.bucketlist.R;

public class GetStartedActivity extends AppCompatActivity {
    Button getstartedButton;
    ImageView ea1,ea2,ea3,ea4,logobucketlist,subSlideBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);
        getstartedButton = (Button)findViewById(R.id.getStartedButton);
        ea1 = (ImageView)findViewById(R.id.ea1);
        ea2 = (ImageView)findViewById(R.id.ea2);
        ea3 = (ImageView)findViewById(R.id.ea3);
        ea4 = (ImageView)findViewById(R.id.ea4);
        logobucketlist = (ImageView)findViewById(R.id.logobucketlist);
    }
    //TO NEXT SLIDE FUNCTION
    public void toNextSlide(View view){
        Intent intent = new Intent(this, First_content.class);

        Pair[] pairs = new Pair[5];
        pairs[0]= new Pair<View,String>(ea1,"ea1transition1");
        pairs[1]= new Pair<View,String>(ea2,"ea2transition1");
        pairs[2]= new Pair<View,String>(ea3,"ea3transition1");
        pairs[3]= new Pair<View,String>(ea4,"ea4transition1");
        pairs[4]=new Pair<View,String>(logobucketlist,"logotransition1");

        Slide slide = new Slide(Gravity.LEFT);
        slide.addTarget(R.id.getStartedButton);
        getWindow().setExitTransition(slide);


        ActivityOptions options =  ActivityOptions.makeSceneTransitionAnimation(this, pairs);

        startActivity(intent,options.toBundle());

    }
}
