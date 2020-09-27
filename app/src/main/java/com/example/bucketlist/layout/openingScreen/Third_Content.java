package com.example.bucketlist.layout.openingScreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.transition.Slide;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;

import com.example.bucketlist.R;
import com.example.bucketlist.layout.loginLayouts.SignupActivity;
import com.example.bucketlist.layout.loginLayouts.LoginByEmailActivity;

public class Third_Content extends AppCompatActivity {
    Button loginbutton,signupbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third__content);

        loginbutton = (Button) findViewById(R.id.loginButton);
        signupbutton= (Button) findViewById(R.id.signupButton);
        Slide slide = new Slide(Gravity.TOP);
        slide.addTarget(R.id.loginButton);
        getWindow().setEnterTransition(slide);



    }
    public void toLoginPage(View view){
        Intent i=new Intent(view.getContext(), LoginByEmailActivity.class);
        startActivity(i);
    }

    public void toSignupPage(View view){
        Intent i=new Intent(view.getContext(),SignupActivity.class);
        startActivity(i);
    }
}