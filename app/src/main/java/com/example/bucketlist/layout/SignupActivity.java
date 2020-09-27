package com.example.bucketlist.layout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bucketlist.ContactEntry;
import com.example.bucketlist.HomeActivity;
import com.example.bucketlist.R;
import com.example.bucketlist.data.UserInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignupActivity extends AppCompatActivity
        implements View.OnClickListener {

    private static final String TAG = "SIGN UP ACTIVITY" ;
    private EditText signUpemailEditText;
    private EditText signUpConfirmPasswordEditText;
    private EditText signUpPasswordEditText;
    private Button signUpButton;
    String email,password;

    FirebaseAuth mAuth;
    FirebaseUser mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        initializeUi();
    }

    private void initializeUi() {
            signUpemailEditText = findViewById(R.id.email_text_view);
            mAuth = FirebaseAuth.getInstance();
            signUpConfirmPasswordEditText = findViewById(R.id.confirm_password_text_view2);
            signUpPasswordEditText  = findViewById(R.id.password_text_view2);
            signUpButton = findViewById(R.id.signup_button);
            signUpButton.setOnClickListener(this);
            Toast.makeText(this, signUpButton.getText().toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.signup_button) {
            doSignUp();
        }
    }

    private void doSignUp() {
        if (TextUtils.isEmpty(signUpemailEditText.getText().toString())
                || TextUtils.isEmpty(signUpPasswordEditText.getText().toString().trim())
                || TextUtils.isEmpty(signUpConfirmPasswordEditText.getText().toString().trim())) {

            if(TextUtils.isEmpty(signUpemailEditText.getText().toString()))
                signUpemailEditText.setError("Enter the email");

            if(TextUtils.isEmpty(signUpPasswordEditText.getText().toString().trim()))
                 signUpPasswordEditText.setError("Enter the password ");

            if(TextUtils.isEmpty(signUpConfirmPasswordEditText.getText().toString().trim()))
                signUpConfirmPasswordEditText.setError("Enter confirm password");


        }

        else if ( signUpPasswordEditText.getText().toString().trim().length() < 8){
            signUpPasswordEditText.setError("password length should be equal or more than 8 character");
        }

        else if(!signUpPasswordEditText.getText().toString().trim().equals(signUpConfirmPasswordEditText.getText().toString().trim())){


            signUpConfirmPasswordEditText.setError("password does not match");
            signUpPasswordEditText.setError("password does not match");
        }

        else {
             email = signUpemailEditText.getText().toString().trim();
             password = signUpConfirmPasswordEditText.getText().toString().trim();

            mAuth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                Log.d(TAG, "onComplete: ");
                                Toast.makeText(SignupActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                                //TODO ADD INTENT HERE
                                startHome();
                            } else {
                                Toast.makeText(SignupActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void startHome() {
        Intent i=new Intent(getApplicationContext(), ContactEntry.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.putExtra("password",password);
        startActivity(i);

    }


    @Override
    protected void onStart() {
        super.onStart();

        if (mAuth != null) {
            mUser = mAuth.getCurrentUser();
        }
    }
}