package com.example.bucketlist;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class OtpActivityRegister extends AppCompatActivity {

    EditText otp;
    Button verifyButton;
    TextView resend;
    String email,password;
    PhoneAuthProvider.ForceResendingToken token;
    String phoneNumber, id;

    private static final String TAG = "MESSAGE ";

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_register);

        email = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("password");
        phoneNumber = getIntent().getStringExtra("phonenumber");

        mAuth = FirebaseAuth.getInstance();

        otp = findViewById(R.id.otp);

        verifyButton = findViewById(R.id.verifyButton);
        resend = findViewById(R.id.resend);





        sendVerificationCode();
        //ON CLICK VERIFICATION BUTTON
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
                    linkCredential(credential);
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
                        linkCredential(phoneAuthCredential);
                        resend.setVisibility(View.INVISIBLE);

                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Toast.makeText(OtpActivityRegister.this, "Failed", Toast.LENGTH_SHORT).show();
                        if (e instanceof FirebaseAuthInvalidCredentialsException){
                            Toast.makeText(OtpActivityRegister.this, "Invalid Number", Toast.LENGTH_SHORT).show();

                        }else if (e instanceof FirebaseTooManyRequestsException){
                            Toast.makeText(OtpActivityRegister.this, "Too many Request", Toast.LENGTH_SHORT).show();
                            resend.setVisibility(View.INVISIBLE);
                        }
                        verifyButton.setEnabled(false);
                        resend.setVisibility(View.INVISIBLE);

                    }


                });
    }

//    public void signInWithPhoneAuthCredential(final PhoneAuthCredential credential) {
//        mAuth.signInWithCredential(credential)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//
//                            FirebaseUser user = task.getResult().getUser();
//                            final AlertDialog.Builder alert;
//                            alert = new AlertDialog.Builder(OtpActivity.this);
//                            View view1 = getLayoutInflater().inflate(R.layout.verify_popup_window,null);
//
//                            alert.setView(view1);
//
//                            final AlertDialog alertDialog = alert.create();
//                            alertDialog.setCanceledOnTouchOutside(false);
//
//                        } else {
//                            Toast.makeText(OtpActivity.this, "Verification failed", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//    }

    private void linkCredential(final AuthCredential credential) {
        mAuth.getCurrentUser().linkWithCredential(credential)
                .addOnCompleteListener(this,new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user = task.getResult().getUser();
                    Toast.makeText(OtpActivityRegister.this, "Merged", Toast.LENGTH_SHORT).show();
                    resend.setVisibility(View.INVISIBLE);


                    final AlertDialog.Builder alert;
                    alert = new AlertDialog.Builder(OtpActivityRegister.this);
                    View view1 = getLayoutInflater().inflate(R.layout.verify_popup_window,null);
                    final Button continueButton = view1.findViewById(R.id.continueButton);
                    alert.setView(view1);

                    final AlertDialog alertDialog = alert.create();
                    alertDialog.setCanceledOnTouchOutside(false);

                    continueButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(OtpActivityRegister.this, UserDetail.class);
                            intent.putExtra("phonenumber",phoneNumber);
                            intent.putExtra("email",email);
                            startActivity(intent);
                        }
                    });
                    alertDialog.show();



                }
                else{
                    Toast.makeText(OtpActivityRegister.this, "failed to merge"+ task.getException().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void linkAndMerge(AuthCredential credential) {

        // [START auth_link_and_merge]
        FirebaseUser prevUser = FirebaseAuth.getInstance().getCurrentUser();
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        FirebaseUser currentUser = task.getResult().getUser();
                        // Merge prevUser and currentUser accounts and data
                        // ...
                    }
                });
        // [END auth_link_and_merge]
    }


}

