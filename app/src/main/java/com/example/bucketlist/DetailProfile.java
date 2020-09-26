package com.example.bucketlist;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Timer;
import java.util.TimerTask;

public class DetailProfile extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG ="Detail Profile" ;
    private Button signoutButton;
    EditText displayName,emailAddress,phoneNumber;
    private ImageView profileImage , editButton , backButton ,doneButton , clearButton,displayNamedoneButton;
           ImageView phonedoneButton, emaildoneButton ;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    String StringImageUri,name,email,phone;
    ProgressBar emailProgress,phoneProgress,nameProgress;
    int count =0;
    Timer timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_detail_profile);


        //initializing view
        initialization();

        displayName.setFocusable(false);
        emailAddress.setFocusable(false);
        phoneNumber.setFocusable(false);



        signoutButton.setOnClickListener(this);
        editButton.setOnClickListener(this);
        backButton.setOnClickListener(this);
        doneButton.setOnClickListener(this);
        clearButton.setOnClickListener(this);

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
        doneButton = findViewById(R.id.doneButton);
        clearButton = findViewById(R.id.clearButton);
       displayNamedoneButton = findViewById(R.id.displayNamedoneButton);
       phonedoneButton = findViewById(R.id.phonedoneButton);
       emaildoneButton = findViewById(R.id.emaildoneButton);
}

   public void onClickSignoutButton(){
       if (firebaseAuth != null) {
           firebaseAuth.signOut();
           Intent i = new Intent(DetailProfile.this,LoginByEmailActivity.class);
           i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
           startActivity(i);
       }
   }

   public void onClickEditButton(){
       editButton.setVisibility(View.INVISIBLE);
       backButton.setVisibility(View.INVISIBLE);

       doneButton.setVisibility(View.VISIBLE);
       clearButton.setVisibility(View.VISIBLE);

       displayName.setFocusableInTouchMode(true);
       emailAddress.setFocusableInTouchMode(true);
       phoneNumber.setFocusableInTouchMode(true);

       displayName.addTextChangedListener(getTextWatcher(displayName , displayNamedoneButton , name));
       emailAddress.addTextChangedListener(getTextWatcher(emailAddress , emaildoneButton, email));
       phoneNumber.addTextChangedListener(getTextWatcher(phoneNumber , phonedoneButton , phone));
    }


    private TextWatcher getTextWatcher(final EditText editText ,final ImageView generalDoneButton ,final String string) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                 if(!string.equals(editText.getText().toString())){
                     generalDoneButton.setVisibility(View.VISIBLE);
                     generalDoneButton.setOnClickListener(DetailProfile.this);
                 }
                 else
                     generalDoneButton.setVisibility(View.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.signoutButton){
            onClickSignoutButton();
        }

        else if (v.getId() == R.id.editButton){
            onClickEditButton();
        }
        else if (v.getId() == R.id.displayNamedoneButton){
            Toast.makeText(this, "Press to save name", Toast.LENGTH_SHORT).show();


        }
        else if (v.getId() == R.id.emaildoneButton){
            Toast.makeText(this, "Press to save email", Toast.LENGTH_SHORT).show();

        }
        else if (v.getId() == R.id.phonedoneButton){
            Toast.makeText(this, "Press to save email", Toast.LENGTH_SHORT).show();


        }

    }


}