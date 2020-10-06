package com.example.bucketlist.layout.loginLayouts;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bucketlist.layout.userLayout.ContactEntry;
import com.example.bucketlist.HomeActivity;
import com.example.bucketlist.R;
import com.example.bucketlist.layout.userLayout.EditProfileActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import static com.example.bucketlist.R.drawable.ic_back_to_active;

public class    LoginByEmailActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "LOGIN ACTIVITY";
    private Button loginButton;
    private EditText emailEditText;
    private EditText passwordEditText;
//    private EditText nameEditText;
//    private EditText signUpemailEditText;
//    private EditText signUppasswordEditText;
    private RelativeLayout loginLayout;
    private TextView signUpTexView;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    String password;
    FirebaseFirestore firebaseFirestore;
    String previousPassword;
    private TextView forgotPassword;
    AlertDialog alertDialog;
//    RelativeLayout login_layout;
//    ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_by_email);

        initializeUi();
    }

    private void initializeUi() {

        mAuth = FirebaseAuth.getInstance();
         firebaseFirestore = FirebaseFirestore.getInstance();
        emailEditText = findViewById(R.id.login_email_text_view);
        passwordEditText = findViewById(R.id.login_password_text_view);
        loginButton  = findViewById(R.id.login_button);
        signUpTexView = findViewById(R.id.text_sign_up);
        loginLayout = findViewById(R.id.login1_layout);
        forgotPassword = findViewById(R.id.forgotPassword);
        forgotPassword.setOnClickListener(this);
        signUpTexView.setOnClickListener(this);
        loginButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.login_button) {
            doLogIn();
        }
        else if (view.getId() == R.id.text_sign_up) {
            Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(getApplicationContext(), SignupActivity.class);
            startActivity(i);

        }
        else if (view.getId() == R.id.forgotPassword){
            final android.app.AlertDialog.Builder builder;
            builder = new AlertDialog.Builder(LoginByEmailActivity.this);
            View view1 = getLayoutInflater().inflate(R.layout.reset_password,null);
            Button sendLinkButton = view1.findViewById(R.id.sendLinkButton);
            TextView feedbackMessage = view1.findViewById(R.id.feedbackMessage);
            final EditText emailEntry = view1.findViewById(R.id.emailEntry);
            ImageView popupClearButton = view1.findViewById(R.id.clearButton);
            final TextView afterMessage = view1.findViewById(R.id.afterMessage);

            builder.setView(view1);
            alertDialog = builder.create();
            alertDialog.setCanceledOnTouchOutside(false);

            popupClearButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });
            sendLinkButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                    if(!emailEntry.getText().toString().matches(emailPattern)||emailEntry.getText().toString().isEmpty()){
                        emailEntry.setError("Invalid Email address");
                    }
                    else {
                        mAuth.sendPasswordResetEmail(emailEntry.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                afterMessage.setText("Password reset link has been sent to registered email address");
                                afterMessage.setVisibility(View.VISIBLE);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @SuppressLint("ResourceAsColor")
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                afterMessage.setText("Error: reset link cannotb be sent on the entered email address. Please check the email address");
                                afterMessage.setTextColor(R.color.colorbottomnav);

                                afterMessage.setVisibility(View.VISIBLE);

                            }
                        });
                    }
                }
            });

            alertDialog.show();
        }


    }


    private void startHome() {
        Intent i=new Intent(getApplicationContext(), HomeActivity.class);

        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.putExtra("password",password);
        i.putExtra("from activity","LoginActivity");
        startActivity(i);

    }

    private void doLogIn() {
        if (TextUtils.isEmpty(emailEditText.getText().toString()) || TextUtils.isEmpty(passwordEditText.getText().toString().trim()) || passwordEditText.getText().toString().trim().length() < 8) {
            Toast.makeText(getApplicationContext(),"Any item can't be empty and min length of password should be greater than  8",Toast.LENGTH_SHORT).show();
        } else {
            String email = emailEditText.getText().toString().trim();
             password = passwordEditText.getText().toString().trim();

            mAuth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                Log.d(TAG, "onComplete: ");
                                Toast.makeText(LoginByEmailActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                                //TODO ADD INTENT HERE FOR NEXT ACTIVITY





                                startHome();
                            } else {
                                Toast.makeText(LoginByEmailActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
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


}
