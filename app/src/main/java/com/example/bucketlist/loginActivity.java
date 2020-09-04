package com.example.bucketlist;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class loginActivity extends AppCompatActivity implements View.OnClickListener {

    Button loginButton;
    EditText emailEditText;
    EditText passwordEditText;
    RelativeLayout loginLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
//
//        getSupportActionBar().setTitle("BucketList");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle("BucketList");
        getSupportActionBar().hide();

        initializeUi();
    }

    private void initializeUi() {

        emailEditText = findViewById(R.id.email_text_iew);
        passwordEditText = findViewById(R.id.password_text_view);
        loginButton  = findViewById(R.id.login_button);
        loginButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.login_button) {
            doLogin();
        }
    }

    private void doLogin() {
        if (TextUtils.isEmpty(emailEditText.getText().toString()) || TextUtils.isEmpty(passwordEditText.getText().toString().trim()) || passwordEditText.getText().toString().trim().length() < 8) {
            Toast.makeText(getApplicationContext(),"Any item can't be epmty and min lebgth of password is 8",Toast.LENGTH_SHORT).show();
        } else {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
        }
    }
}
