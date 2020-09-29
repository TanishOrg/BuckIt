package com.example.bucketlist.layout.userLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.bucketlist.HomeActivity;
import com.example.bucketlist.R;
import com.example.bucketlist.layout.loginLayouts.OtpActivityRegister;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView editProfileImage;
    ImageView clearButton, doneButton;
    ImageView phoneDoneButton, emailDoneButton, nameDoneButton;
    EditText editDisplayName, editEmailAddress, editPhoneNumber;
    FirebaseAuth mAuth;
    FirebaseFirestore firebaseFirestore;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    String newStringImageUri,StringImageUri, name, email, phone ,password;
    Uri imageUri;
    String passwordEntry;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+",
            phonePattern = "^[+][0-9]{9,14}$" ;
    String id;
    TextView feedbackMessage;
    Button verifyButton;
    PhoneAuthProvider.ForceResendingToken token;
    EditText otpEntry;
    AlertDialog alertDialog;
    ImageView popupClearButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference().child(mAuth.getCurrentUser().getUid());

        initialization();

        editProfileImage.setOnClickListener(this);

        clearButton.setOnClickListener(this);

    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser mUser = mAuth.getCurrentUser();

        DocumentReference documentReference = firebaseFirestore.collection("Users").document(mUser.getUid());

        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                if (error != null) {
                    Log.d("edit profile", "Error:" + error.getMessage());
                } else {
                    name = value.getString("Display Name");
                    editDisplayName.setText(name);

                    email = value.getString("Email Address");
                    editEmailAddress.setText(email);

                    phone = value.getString("Phone Number");
                    editPhoneNumber.setText(phone);

                    StringImageUri = value.getString("Image Uri");
                    Glide.with(getApplicationContext()).load(StringImageUri).into(editProfileImage);

                    Log.i("edit profile", "String uri: " + StringImageUri);

                    password = value.getString("User password");
                }

                editDisplayName.addTextChangedListener(getTextWatcher(editDisplayName,nameDoneButton,name));
                editEmailAddress.addTextChangedListener(getTextWatcher(editEmailAddress,emailDoneButton,email));
                editPhoneNumber.addTextChangedListener(getTextWatcher(editPhoneNumber,phoneDoneButton,phone));
            }
        });


    }

    private void initialization(){

        editDisplayName = findViewById(R.id.editDisplayName);
        editEmailAddress = findViewById(R.id.editEmailAddress);
        editProfileImage = findViewById(R.id.editProfileImage);
        editPhoneNumber = findViewById(R.id.editPhoneNumber);
        doneButton = findViewById(R.id.clearButton);
        clearButton = findViewById(R.id.clearButton);
        phoneDoneButton = findViewById(R.id.phoneDoneButton);
        emailDoneButton = findViewById(R.id.emailDoneButton);
        nameDoneButton = findViewById(R.id.nameDoneButton);

    }


    public TextWatcher getTextWatcher(final EditText generalEditText,final ImageView generalDoneButton, final String string){
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!string.equals(generalEditText.getText().toString()) && !generalEditText.getText().toString().isEmpty()){
                    generalDoneButton.setVisibility(View.VISIBLE);
                    generalDoneButton.setOnClickListener(EditProfileActivity.this);
                }

                else {
                    generalDoneButton.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
    }

    public void updateName(){
        FirebaseUser mUser = mAuth.getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();
        DocumentReference documentReference = firebaseFirestore.collection("Users").document(mUser.getUid());
        documentReference.update("Display Name",editDisplayName.getText().toString());


    }  //updating Name

    private   void updateImage(String newStringImageUri){
        firebaseFirestore = FirebaseFirestore.getInstance();
        DocumentReference documentReference = firebaseFirestore.collection("Users").document(mAuth.getCurrentUser().getUid());
        documentReference.update("Image Uri",newStringImageUri);
        Toast.makeText(this, "Name Updated", Toast.LENGTH_SHORT).show();

    }   //updating profile Image

    private void updateEmail(String updatedEmail){
        firebaseFirestore = FirebaseFirestore.getInstance();
        DocumentReference documentReference = firebaseFirestore.collection("Users").document(mAuth.getCurrentUser().getUid());
        documentReference.update("Email Address",updatedEmail);
        emailDoneButton.setVisibility(View.INVISIBLE);
        Toast.makeText(this, "Email Updated", Toast.LENGTH_SHORT).show();

    }

    public void changePhoneNumberFirestore(){
        firebaseFirestore = FirebaseFirestore.getInstance();
        DocumentReference documentReference = firebaseFirestore.collection("Users").document(mAuth.getCurrentUser().getUid());
        documentReference.update("Phone Number",editPhoneNumber.getText().toString());
        phoneDoneButton.setVisibility(View.INVISIBLE);
        alertDialog.dismiss();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == Activity.RESULT_OK){
                imageUri = result.getUri();
                editProfileImage.setImageURI(imageUri);
                uploadImageToFirebase(imageUri);
            }
        }
    }

    private void uploadImageToFirebase(Uri imageUri) {
        //upload image to firebase storage
        final StorageReference fileRef  = storageReference.child("profileImage.jpeg");

        fileRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.d("Sucess", "onSuccess: ");
                        fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                newStringImageUri = uri.toString();
                                updateImage(newStringImageUri);
                                Log.i("fieref", "onEvent: "+fileRef.toString());
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle any errors
                            }
                        });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Sucess", "onFailure: ");
                    }
                });

    }//uploadImageToFirebase

    public void alertforEntryPassword(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this,R.style.AlertDialog));
        builder.setTitle("Password");
        builder.setMessage("Enter password to change email");

        final EditText input = new EditText(this);
        input.setBackgroundResource(R.color.white);
        input.setGravity(Gravity.CENTER);


        input.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);


        builder.setCancelable(false);

        builder.setPositiveButton("CHANGE EMAIL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                 passwordEntry = input.getText().toString();
                 if(passwordEntry.equals(password)){
                     reAuthenticateEmail(email,passwordEntry);
                 }
                 else {
                     Toast.makeText(EditProfileActivity.this, "Incorrect password", Toast.LENGTH_SHORT).show();
                 }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public void alertForEntryOtp(){
        final AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(EditProfileActivity.this);
        View view1 = getLayoutInflater().inflate(R.layout.edit_phone_otp_popup,null);
         verifyButton = view1.findViewById(R.id.verifyButton);
        feedbackMessage = view1.findViewById(R.id.feedbackMessage);
        otpEntry = view1.findViewById(R.id.otpEntry);
        popupClearButton = view1.findViewById(R.id.clearButton);

        builder.setView(view1);
         alertDialog = builder.create();
         alertDialog.setCanceledOnTouchOutside(false);

        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(otpEntry.getText().toString())){
                    otpEntry.setError("Enter the OTP");
                }

                else  if(otpEntry.getText().toString().replace(" ","").length()!=6){
                    otpEntry.setError("Enter the valid OTP");
                }

                else{
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(id,otpEntry.getText().toString().replace(" ",""));
                    updatingPhoneNumber(credential);

                }
            }
        });

        feedbackMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendVerificationCode();
            }
        });

        popupClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();



    }

    private void sendVerificationCode() {
        new CountDownTimer(60000,1000){

            @Override
            public void onTick(long millisUntilFinished) {
                feedbackMessage.setText("Regenerate OTP in " + millisUntilFinished/1000 + " seconds");
                feedbackMessage.setEnabled(false);
            }

            @Override
            public void onFinish() {
                feedbackMessage.setText("Resend OTP");
                feedbackMessage.setEnabled(true);

            }
        }.start();

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                editPhoneNumber.getText().toString(),        // Phone number to verify
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
                        Log.d("message", "onVerificationCompleted:" + phoneAuthCredential);
                       updatingPhoneNumber(phoneAuthCredential);
                        feedbackMessage.setVisibility(View.INVISIBLE);


                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Toast.makeText(EditProfileActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                        if (e instanceof FirebaseAuthInvalidCredentialsException){
                            Toast.makeText(EditProfileActivity.this, "Invalid Number", Toast.LENGTH_SHORT).show();

                        }else if (e instanceof FirebaseTooManyRequestsException){
                            Toast.makeText(EditProfileActivity.this, "Too many Request", Toast.LENGTH_SHORT).show();
                            feedbackMessage.setVisibility(View.INVISIBLE);
                        }
                        verifyButton.setEnabled(false);
                        feedbackMessage.setVisibility(View.INVISIBLE);

                    }


                });
    }

    public void updatingPhoneNumber (PhoneAuthCredential credential){
        FirebaseUser mUser = mAuth.getCurrentUser();
        mUser.updatePhoneNumber(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(EditProfileActivity.this, "Phone number Updated", Toast.LENGTH_SHORT).show();
                    changePhoneNumberFirestore();
                }
                else
                    Toast.makeText(EditProfileActivity.this, "Not Upgraded", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void reAuthenticateEmail(final String email, final String password){

        FirebaseUser mUser = mAuth.getCurrentUser();
        AuthCredential credential = EmailAuthProvider
                .getCredential(email, password);
        mUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d("email Checking", "User re-authenticated.");
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                user.updateEmail(editEmailAddress.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            updateEmail(editEmailAddress.getText().toString());
                            Toast.makeText(EditProfileActivity.this, "User email address updated.", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        });
    }

    public void onClickClearButton(){
        Intent i = new Intent(EditProfileActivity.this, DetailProfile.class);
        startActivity(i);

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.nameDoneButton){
            Pattern my_pattern = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
            Matcher my_match = my_pattern.matcher(editDisplayName.getText().toString());
            boolean check = my_match.find();
            if(check){
                editDisplayName.setError("Display name should not contain special character");
                nameDoneButton.setVisibility(View.INVISIBLE);
            }
            else {
                nameDoneButton.setVisibility(View.VISIBLE);
                updateName();

            }

            nameDoneButton.setVisibility(View.INVISIBLE);
        }

        else if (v.getId() == R.id.emailDoneButton){

            if(!editEmailAddress.getText().toString().matches(emailPattern)){
                editEmailAddress.setError("Invalid Email address");
                emailDoneButton.setVisibility(View.INVISIBLE);
            }
            else {
                emailDoneButton.setVisibility(View.VISIBLE);
                alertforEntryPassword();

            }
        }

        else if (v.getId() == R.id.editProfileImage){
            CropImage.activity().start(EditProfileActivity.this);
        }

        else if (v.getId() == R.id.clearButton){
            onClickClearButton();
        }

        else if (v.getId() == R.id.phoneDoneButton){
            if(!editPhoneNumber.getText().toString().matches(phonePattern)){
                if(!editPhoneNumber.getText().toString().contains("+")){
                    editPhoneNumber.setError("Invalid Phone number. Please include + ");
                    phoneDoneButton.setVisibility(View.INVISIBLE);
                }
                else {
                    editPhoneNumber.setError("Invalid Phone number.");
                    phoneDoneButton.setVisibility(View.INVISIBLE);
                }
            }
            else{
                Toast.makeText(this, "Valid", Toast.LENGTH_SHORT).show();
                alertForEntryOtp();
                sendVerificationCode();
            }

        }





    }
}