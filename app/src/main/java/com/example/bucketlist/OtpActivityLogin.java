package com.example.bucketlist;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bucketlist.layout.loginLayouts.OtpActivityRegister;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class OtpActivityLogin extends AppCompatActivity {
    EditText otp;
    Button verifyButton;
    TextView resend;
    PhoneAuthProvider.ForceResendingToken token;
    String phoneNumber, id;
    private static final String TAG = "MESSAGE ";

    private FirebaseAuth mAuth;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_register);

        phoneNumber = getIntent().getStringExtra("phone number");
        Toast.makeText(this, phoneNumber, Toast.LENGTH_SHORT).show();

        mAuth = FirebaseAuth.getInstance();

        otp = findViewById(R.id.otp);

        verifyButton = findViewById(R.id.verifyButton);
        resend = findViewById(R.id.resend);

        sendVerificationCode();

        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(otp.getText().toString())){
                    otp.setError("Enter the OTP");
                }

                else  if(otp.getText().toString().replace(" ","").length()!=6){
                    otp.setError("Enter the valid OTP");
                }

                else{
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(id, otp.getText().toString().replace(" ",""));
                    signInWithPhoneAuthCredential(credential);
                }
            }
        });

    }



    private void sendVerificationCode() {
        new CountDownTimer(60000,1000){

            @Override
            public void onTick(long millisUntilFinished) {
                resend.setText("Regenerate OTP in " + millisUntilFinished/1000 + " seconds");
                resend.setEnabled(false);
            }

            @Override
            public void onFinish() {
                resend.setText("Resend OTP");
                resend.setEnabled(true);

            }
        }.start();

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {

                        id = s;
                        token = forceResendingToken;
                    }

                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        Log.d(TAG, "onVerificationCompleted:" + phoneAuthCredential);
                        signInWithPhoneAuthCredential(phoneAuthCredential);
                        resend.setVisibility(View.INVISIBLE);

                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Toast.makeText(OtpActivityLogin.this, "Failed", Toast.LENGTH_SHORT).show();
                        if (e instanceof FirebaseAuthInvalidCredentialsException){
                            Toast.makeText(OtpActivityLogin.this, "Invalid Number", Toast.LENGTH_SHORT).show();

                        }else if (e instanceof FirebaseTooManyRequestsException){
                            Toast.makeText(OtpActivityLogin.this, "Too many Request", Toast.LENGTH_SHORT).show();
                            resend.setVisibility(View.INVISIBLE);
                        }
                        verifyButton.setEnabled(false);
                        resend.setVisibility(View.INVISIBLE);

                    }


                });
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential){
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Intent i=new Intent(getApplicationContext(), HomeActivity.class);
                    i.putExtra("from activity", "LoginByPhoneActivity");
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    startActivity(i);
                }
                else{
                    Toast.makeText(OtpActivityLogin.this, "Cannot SignIn with Phone number", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
