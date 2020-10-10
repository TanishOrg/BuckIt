package com.example.bucketlist;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bucketlist.layout.loginLayouts.OtpActivityRegister;
import com.example.bucketlist.layout.userLayout.ContactEntry;
import com.hbb20.CountryCodePicker;

public class ContactEntyLogin extends AppCompatActivity {

    EditText phoneNumber;
    CountryCodePicker codeNumber;
    TextView errorText;
    Button generateotpButton;
    ProgressBar generateProgressBar;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_entry);

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
                    Intent i = new Intent(getApplicationContext(), OtpActivityLogin.class);
                    i.putExtra("phone number",codeNumber.getFullNumberWithPlus().replace(" ",""));
                    finish();
                    startActivity(i);
                }
            }
        });
    }


}
