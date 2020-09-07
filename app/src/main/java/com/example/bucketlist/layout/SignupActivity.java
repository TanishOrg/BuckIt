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

import com.example.bucketlist.HomeActivity;
import com.example.bucketlist.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignupActivity extends AppCompatActivity
        implements View.OnClickListener {

    private static final String TAG = "SIGN UP ACTIVITY" ;
    private EditText signUpemailEditText;
    private EditText nameEditText;
    private TextView signUpPasswordEditText;
    private Button signUpButton;
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
            nameEditText = findViewById(R.id.name_text_view);
            signUpPasswordEditText  = findViewById(R.id.password_text_view2);
            signUpButton = findViewById(R.id.signup_button);
            signUpButton.setOnClickListener(this);
            Toast.makeText(this, signUpButton.getText().toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.signup_button) {
            Toast.makeText(this, "Button Sign Up", Toast.LENGTH_SHORT).show();
            doSignUp();
        }
    }

    private void doSignUp() {
        if (TextUtils.isEmpty(signUpemailEditText.getText().toString())
                || TextUtils.isEmpty(signUpPasswordEditText.getText().toString().trim())
                || signUpPasswordEditText.getText().toString().trim().length() < 8
                || TextUtils.isEmpty(nameEditText.getText().toString().trim())) {
            Toast.makeText(getApplicationContext(),"Any item can't be empty and min length of password should be greater than  8",Toast.LENGTH_SHORT).show();
        } else {
            String email = signUpemailEditText.getText().toString().trim();
            String password = signUpPasswordEditText.getText().toString().trim();

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
        Intent i=new Intent(getApplicationContext(), HomeActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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