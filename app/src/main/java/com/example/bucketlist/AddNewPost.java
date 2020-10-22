package com.example.bucketlist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddNewPost extends AppCompatActivity implements View.OnClickListener {

    FirebaseAuth mAuth;
    FirebaseUser user;
    ImageView backButton;
    TextView postButton;
    EditText titleText, descriptionText, locationText;
    ChipGroup chipGroup;
    Context context;
    AddNewPost addNewPost;
    FirebaseFirestore firestore;
    boolean empty = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_post);



        initialize();



    }

    private void initialize(){

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        firestore = FirebaseFirestore.getInstance();

        backButton = findViewById(R.id.backButton);
        postButton = findViewById(R.id.postButton);
        titleText = findViewById(R.id.titleText);
        descriptionText = findViewById(R.id.descriptionText);
        locationText = findViewById(R.id.location);
        chipGroup = findViewById(R.id.chipGroup);

        backButton.setOnClickListener(this);
        postButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.backButton){
            Toast.makeText(context, "back", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(context,HomeActivity.class);
            startActivity(i);

        }
        else if (view.getId() == R.id.postButton){
            if (checkEmptyField(empty)){
                Snackbar.make(view,"Please fill all the fields",Snackbar.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(context, "Post shared", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(context,HomeActivity.class);
                startActivity(i);
            }
        }
    }

    private boolean checkEmptyField(boolean empty ){
        if (titleText.getText().toString().isEmpty()||
                descriptionText.getText().toString().isEmpty()||
                locationText.getText().toString().isEmpty()){

            empty = true;

        }
        return empty;
    }




}