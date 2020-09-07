package com.example.bucketlist.layout;

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

import com.example.bucketlist.HomeActivity;
import com.example.bucketlist.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class loginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "LOGIN ACTIVITY";
    private Button loginButton;
//    private  Button signUpButton;
    private EditText emailEditText;
    private EditText passwordEditText;
//    private EditText nameEditText;
//    private EditText signUpemailEditText;
//    private EditText signUppasswordEditText;
    private RelativeLayout loginLayout;
    private TextView signUpTexView;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
//    RelativeLayout login_layout;
//    ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        initializeUi();
    }

    private void initializeUi() {

        mAuth = FirebaseAuth.getInstance();
        
        emailEditText = findViewById(R.id.login_email_text_view);
        passwordEditText = findViewById(R.id.login_password_text_view);
        loginButton  = findViewById(R.id.login_button);
        signUpTexView = findViewById(R.id.text_sign_up);
        loginLayout = findViewById(R.id.login1_layout);
//        constraintLayout = findViewById(R.id.constrain_signup);
        signUpTexView.setOnClickListener(this);
        loginButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.login_button) {
            doLogIn();
        } else if (view.getId() == R.id.text_sign_up) {
            Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(getApplicationContext(), SignupActivity.class);
            startActivity(i);

        }
    }


    private void startHome() {
        Intent i=new Intent(getApplicationContext(), HomeActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);

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
                                startHome();
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
//        mAuth.signOut();
    }
}
