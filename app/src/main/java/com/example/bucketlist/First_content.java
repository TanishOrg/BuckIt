package com.example.bucketlist;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.transition.Slide;
import android.util.Pair;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class First_content extends AppCompatActivity {

    ImageView nextButton;
    ImageView ea1,ea2,ea3,ea4,ea5,logobucketlist,slideBar,subSlideBar;
    TextView skipbutton,firstslidetext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_content);

        nextButton = (ImageView) findViewById(R.id.nextButton);
        ea1 = (ImageView)findViewById(R.id.ea1);
        ea2 = (ImageView)findViewById(R.id.ea2);
        ea3 = (ImageView)findViewById(R.id.ea3);
        ea4 = (ImageView)findViewById(R.id.ea4);
        ea5 = (ImageView)findViewById(R.id.ea5);
        logobucketlist = (ImageView)findViewById(R.id.logobucketlist);
        slideBar = (ImageView)findViewById(R.id.slideBar);
        subSlideBar = (ImageView)findViewById(R.id.subSlideBar);
        skipbutton = (TextView)findViewById(R.id.skipbutton);
        firstslidetext = (TextView)findViewById(R.id.firstslidetext);
    }

    public void toSecondSlide(View view){
        Intent i = new Intent(this,SecondContent.class);
        Pair[] pair = new Pair[11];
        pair[0]= new Pair<View,String>(ea1,"ea1transition1");
        pair[1]= new Pair<View,String>(ea2,"ea2transition1");
        pair[2]= new Pair<View,String>(ea3,"ea3transition1");
        pair[3]= new Pair<View,String>(ea4,"ea4transition1");
        pair[4]= new Pair<View,String>(logobucketlist,"logotransition1");
        pair[5]= new Pair<View,String>(subSlideBar,"slidebartransition1");
        pair[6]= new Pair<View,String>(nextButton,"nextbuttontransition");
        pair[7]= new Pair<View,String>(skipbutton,"skiptransition");
        pair[8]= new Pair<View,String>(firstslidetext,"texttransition1");
        pair[9]= new Pair<View,String>(slideBar,"mainslidetransition");
        pair[10]= new Pair<View,String>(ea5,"ea5transition1");

        ActivityOptions options =  ActivityOptions.makeSceneTransitionAnimation(this, pair);
        startActivity(i,options.toBundle());
    }

    public void skipToLast(View view){
        Intent i = new Intent(this,Third_Content.class);
        Pair[] pair = new Pair[9];
        pair[0]= new Pair<View,String>(ea1,"ea1transition1");
        pair[1]= new Pair<View,String>(ea2,"ea2transition1");
        pair[2]= new Pair<View,String>(ea3,"ea3transition1");
        pair[3]= new Pair<View,String>(ea4,"ea4transition1");
        pair[4]= new Pair<View,String>(logobucketlist,"logotransition1");
        pair[5]= new Pair<View,String>(subSlideBar,"slidebartransition1");
        // pair[6]= new Pair<View,String>(nextButton,"nextbuttontransition");
        // pair[6]= new Pair<View,String>(skipbutton,"skiptransition");
        pair[6]= new Pair<View,String>(firstslidetext,"texttransition1");
        pair[7]= new Pair<View,String>(slideBar,"mainslidetransition");
        pair[8]= new Pair<View,String>(ea5,"ea5transition1");

        Slide slide = new Slide(Gravity.RIGHT);
        slide.addTarget(R.id.nextButton);
        getWindow().setExitTransition(slide);

        ActivityOptions options =  ActivityOptions.makeSceneTransitionAnimation(this, pair);
        startActivity(i,options.toBundle());

    }
}