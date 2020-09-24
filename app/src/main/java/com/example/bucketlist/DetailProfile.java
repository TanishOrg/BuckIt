package com.example.bucketlist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DetailProfile extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG ="Detail Profile" ;
    private Button signoutButton;
    EditText displayName,emailAddress,phoneNumber;
    private ImageView profileImage;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_detail_profile);

        //casting
        signoutButton = findViewById(R.id.signoutButton);
        displayName = findViewById(R.id.displayName);
        emailAddress = findViewById(R.id.emailAddress);
        profileImage = findViewById(R.id.profileImage);
        phoneNumber = findViewById(R.id.phoneNumber);

        signoutButton.setOnClickListener(this);



    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser mUser = firebaseAuth.getCurrentUser();
        Log.d(TAG, "onStart: " + firebaseAuth.getCurrentUser().getDisplayName());
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.signoutButton){
            if (firebaseAuth != null) {
                firebaseAuth.signOut();
                Intent i = new Intent(DetailProfile.this,LoginByEmailActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        }
    }
}