package com.example.bucketlist.layout.loginLayouts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bucketlist.HomeActivity;
import com.example.bucketlist.R;
import com.example.bucketlist.layout.userLayout.UserDetail;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApiNotAvailableException;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class OtpActivityLogin extends AppCompatActivity {

    EditText editTextPhone, editTextOtp,otp;
    FirebaseAuth mAuth;
    String codeSent;
    Button verifyButton;
    TextView resend;
    String phoneNumber, id,password;
    PhoneAuthProvider.ForceResendingToken token;
    private static final String TAG = "MESSAGE ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_register);

        Toast.makeText(OtpActivityLogin.this, "otpActivity", Toast.LENGTH_SHORT).show();

        mAuth = FirebaseAuth.getInstance();

        otp = findViewById(R.id.otp);

        verifyButton = findViewById(R.id.verifyButton);

        phoneNumber = getIntent().getStringExtra("phonenumber");


//        editTextPhone = findViewById(R.id.otp2);
//        editTextOtp = findViewById(R.id.enterOtp);

        resend = findViewById(R.id.resend);


        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(otp.getText().toString())) {
                    otp.setError("Enter the OTP");
                } else if (otp.getText().toString().replace(" ", "").length() != 6) {
                    otp.setError("Enter the valid OTP");
                } else {

                    String code = otp.getText().toString();
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent, code);
                    signInWithPhoneAuthCredential(credential);
//                    Log.d(TAG, "onVerificationCompleted:" + codeSent);
//                    try {
//
//                    }catch (Exception e){
//                        e.printStackTrace();
//                    }

                }
            }
        });
        //ON CLICK RESEND BUTTON
        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendVerificationCode();
            }
        });
    }

    private void sendVerificationCode() {
        new CountDownTimer(60000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                resend.setText("Regenerate OTP in " + millisUntilFinished / 1000 + " seconds");
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

                        codeSent = s;
                        Log.d(TAG, "onVerificationCompleted:" + s);

//                        token = forceResendingToken;
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
                        if (e instanceof FirebaseAuthInvalidCredentialsException) {
                            Toast.makeText(OtpActivityLogin.this, "Invalid Number", Toast.LENGTH_SHORT).show();

                        } else if (e instanceof FirebaseTooManyRequestsException) {
                            Toast.makeText(OtpActivityLogin.this, "Too many Request", Toast.LENGTH_SHORT).show();
                            resend.setVisibility(View.INVISIBLE);
                        }
                     verifyButton.setEnabled(false);
                        resend.setVisibility(View.INVISIBLE);

                    }


                });
    }

    private void signInWithPhoneAuthCredential(final PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                                FirebaseUser user = task.getResult().getUser();
                                Toast.makeText(OtpActivityLogin.this, "Signed in", Toast.LENGTH_SHORT).show();
                                resend.setVisibility(View.INVISIBLE);


                                final AlertDialog.Builder alert;
                                alert = new AlertDialog.Builder(OtpActivityLogin.this);
                                View view1 = getLayoutInflater().inflate(R.layout.verify_popup_window,null);

                                final Button continueButton = view1.findViewById(R.id.continueButton);
                                alert.setView(view1);

                                final AlertDialog alertDialog = alert.create();
                                alertDialog.setCanceledOnTouchOutside(false);

                                continueButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(OtpActivityLogin.this, HomeActivity.class);
//                                        intent.putExtra("password",password);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                                alertDialog.show();
                            }
                            else{
                                Toast.makeText(OtpActivityLogin.this, "failed to merge"+ task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                    }

                });
    }

//    private void startHome() {
//        Intent i = new Intent(getApplicationContext(), HomeActivity.class);
//        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(i);
//
//    }
}
//    private void sendVerificationCode() {
//
//        String phone = editTextPhone.getText().toString();
//        if (phone.isEmpty()) {
//            editTextPhone.setError("Phone Number is required");
//            editTextPhone.requestFocus();
//            return;
//        }
//        if (phone.length() < 10) {
//            editTextPhone.setError("Phone Number invalid");
//            editTextPhone.requestFocus();
//            return;
//        }
//
//        PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//            @Override
//            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
//
//            }
//
//            @Override
//            public void onVerificationFailed(@NonNull FirebaseException e) {
//
//            }
//
//            @Override
//            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
//                super.onCodeSent(s, forceResendingToken);
//                codeSent = s;
//            }
//        };
//        PhoneAuthProvider.getInstance().verifyPhoneNumber(
//                phone,        // Phone number to verify
//                60,                 // Timeout duration
//                TimeUnit.SECONDS,   // Unit of timeout
//                this,
//                mCallbacks);
//
//    }





//        findViewById(R.id.otpButton).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                sendVerificationCode();
//
//            }
//        });
//        findViewById(R.id.signInButton).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                verifySigninCode();
//
//            }
//        });
//
//    }

            //    private void verifySigninCode(){
//        String code=editTextOtp.getText().toString();
//
//        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent, code);
//        signInWithPhoneAuthCredential(credential);
//
//    }



