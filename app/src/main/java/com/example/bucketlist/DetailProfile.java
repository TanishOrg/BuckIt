package com.example.bucketlist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;

public class DetailProfile extends AppCompatActivity implements View.OnClickListener {

    private Button signoutButton;
    EditText displayName,emailAddress,phoneNumber;
    private ImageView profileImage;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_profile);

        //casting
        signoutButton = findViewById(R.id.signoutButton);
        displayName = findViewById(R.id.displayName);
        emailAddress = findViewById(R.id.emailAddress);
        profileImage = findViewById(R.id.profileImage);
        phoneNumber = findViewById(R.id.phoneNumber);

        signoutButton.setOnClickListener(this);

        firebaseAuth = FirebaseAuth.getInstance();

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.signoutButton){
            firebaseAuth.signOut();
            Intent i = new Intent(DetailProfile.this,LoginByEmailActivity.class);
            startActivity(i);
        }
    }
}