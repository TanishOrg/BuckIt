package com.example.bucketlist.layout.userLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.bucketlist.HomeActivity;
import com.example.bucketlist.R;
import com.example.bucketlist.layout.loginLayouts.LoginByEmailActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class DetailProfile extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG ="Detail Profile" ;
    private Button signoutButton;
    EditText displayName,emailAddress,phoneNumber;
    private ImageView profileImage , editButton , backButton ;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    String StringImageUri,name,email,phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_detail_profile);


        //initializing view
        initialization();

        signoutButton.setOnClickListener(this);
        editButton.setOnClickListener(this);
        backButton.setOnClickListener(this);




    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser mUser = firebaseAuth.getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();
        DocumentReference documentReference = firebaseFirestore.collection("Users").document(mUser.getUid());

        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                if (error!=null){
                    Log.d(TAG,"Error:"+error.getMessage());
                }
                else {
                    name = value.getString("Display Name");
                    displayName.setText(name);

                    email =  value.getString("Email Address");
                    emailAddress.setText(email);

                    phone = value.getString("Phone Number");
                    phoneNumber.setText(phone);

                    StringImageUri = value.getString("Image Uri");
                    Glide.with(getApplicationContext()).load(StringImageUri).into(profileImage);

                    Log.i(TAG, "String: "+StringImageUri);
                }

            }
        });


    }





   private void initialization(){
        signoutButton = findViewById(R.id.signoutButton);
        displayName = findViewById(R.id.displayName);
        emailAddress = findViewById(R.id.emailAddress);
        profileImage = findViewById(R.id.profileImage);
        phoneNumber = findViewById(R.id.phoneNumber);
        editButton = findViewById(R.id.editButton);
        backButton = findViewById(R.id.backButton);


}

   public void onClickSignoutButton(){
       if (firebaseAuth != null) {
           firebaseAuth.signOut();
           Intent i = new Intent(DetailProfile.this, LoginByEmailActivity.class);
           i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
           startActivity(i);
       }
   }

   public void onClickEditButton(){
        Intent i = new Intent(DetailProfile.this,EditProfileActivity.class);
        startActivity(i);

    }


    public void onClickBackButton(){
        Intent i = new Intent(DetailProfile.this, HomeActivity.class);
        startActivity(i);

    }




    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.signoutButton){
            onClickSignoutButton();
        }

        else if (v.getId() == R.id.editButton){
            onClickEditButton();
        }
        else if (v.getId() == R.id.backButton){
            onClickBackButton();
        }


    }


}