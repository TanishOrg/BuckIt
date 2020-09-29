package com.example.bucketlist.layout.userLayout;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.bucketlist.R;
import com.example.bucketlist.layout.loginLayouts.OtpActivityRegister;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

public class ContactEntry extends AppCompatActivity {

    EditText phoneNumber;
    CountryCodePicker codeNumber;
    TextView errorText;
    String password;
    Button generateotpButton;
    ProgressBar generateProgressBar;
    private  PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_entry);

        password = getIntent().getStringExtra("password");
        phoneNumber = findViewById(R.id.phoneNumberLayout);
        codeNumber = findViewById(R.id.codeNumber);

        generateotpButton = findViewById(R.id.generateotpButton);
        errorText = findViewById(R.id.errorText);
        generateProgressBar = findViewById(R.id.generateProgressBar);
        codeNumber.registerCarrierNumberEditText(phoneNumber);


        generateotpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(phoneNumber.getText().toString())){
                    errorText.setVisibility(View.VISIBLE);
                    errorText.setText("Please enter your phone number");
                }
                else if(phoneNumber.getText().toString().replace(" ","").length()>15 && phoneNumber.getText().toString().replace(" ","").length()<9){
                    errorText.setText("Invalid Phone number");
                    errorText.setVisibility(View.VISIBLE);

                }
                else {
                    generateProgressBar.setVisibility(View.VISIBLE);
                    Intent i = new Intent(ContactEntry.this, OtpActivityRegister.class);
                    i.putExtra("phonenumber",codeNumber.getFullNumberWithPlus().replace(" ",""));
                    i.putExtra("password",password);
                    startActivity(i);
                }

            }
        });


    }





}