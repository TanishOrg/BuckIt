package com.example.bucketlist;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class loginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "LOGIN ACTIVITY";
    private Button loginButton;
    private  Button signUpButton;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText nameEditText;
    private EditText signUpemailEditText;
    private EditText signUppasswordEditText;
    private RelativeLayout loginLayout;
    private TextView signUpTexView;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    RelativeLayout login_layout;
    ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        Toolbar toolbar=(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//
//        getSupportActionBar().setTitle("BucketList");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle("Login");
//        getSupportActionBar().hide();

        initializeUi();
    }

    private void initializeUi() {

        mAuth = FirebaseAuth.getInstance();
        
        emailEditText = findViewById(R.id.email_text_view1);
        passwordEditText = findViewById(R.id.password_text_view);
        loginButton  = findViewById(R.id.login_button);
        signUpTexView = findViewById(R.id.text_sign_up);
        loginLayout = findViewById(R.id.login1_layout);
        constraintLayout = findViewById(R.id.constrain_signup);
        signUpTexView.setOnClickListener(this);
        loginButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.login_button) {
            doLogIn();
            Intent intent = new Intent(this,MainActivity2.class);
            startActivity(intent);
        } else if (view.getId() == R.id.text_sign_up) {
//            Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show();
            View v = getLayoutInflater().inflate(R.layout.signup,constraintLayout);
            constraintLayout.setVisibility(View.VISIBLE);
            loginLayout.setVisibility(View.INVISIBLE);

            signUpemailEditText = v.findViewById(R.id.email_text_view);
            nameEditText = v.findViewById(R.id.name_text_view);
            signUppasswordEditText  = v.findViewById(R.id.password_text_view2);
            signUpButton = v.findViewById(R.id.signup_button);
            signUpButton.setOnClickListener(this);

        } else if (view.getId() == R.id.signup_button) {
            Toast.makeText(this, "Button Sign Up", Toast.LENGTH_SHORT).show();
            doSignUp();
        }
    }

    private void doSignUp() {
        if (TextUtils.isEmpty(signUpemailEditText.getText().toString())
                || TextUtils.isEmpty(signUppasswordEditText.getText().toString().trim())
                || passwordEditText.getText().toString().trim().length() < 8
                || TextUtils.isEmpty(nameEditText.getText().toString().trim())) {
            Toast.makeText(getApplicationContext(),"Any item can't be empty and min length of password should be greater than  8",Toast.LENGTH_SHORT).show();
        } else {
            String email = signUpemailEditText.getText().toString().trim();
            String password = signUppasswordEditText.getText().toString().trim();

            mAuth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                Log.d(TAG, "onComplete: ");
                                Toast.makeText(loginActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                                //TODO ADD INTENT HERE
                            } else {
                                Toast.makeText(loginActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void doLogIn() {
        if (TextUtils.isEmpty(emailEditText.getText().toString()) || TextUtils.isEmpty(passwordEditText.getText().toString().trim()) || passwordEditText.getText().toString().trim().length() < 8) {
            Toast.makeText(getApplicationContext(),"Any item can't be empty and min length of password should be greater than  8",Toast.LENGTH_SHORT).show();
        } else {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            mAuth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                Log.d(TAG, "onComplete: ");
                                Toast.makeText(loginActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                                //TODO ADD INTENT HERE FOR NEXT ACTIVITY
                            } else {
                                Toast.makeText(loginActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth != null) {
            mUser = mAuth.getCurrentUser();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mAuth.signOut();
    }
}
