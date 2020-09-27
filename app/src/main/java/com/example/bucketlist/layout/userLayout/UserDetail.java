package com.example.bucketlist.layout.userLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.bucketlist.HomeActivity;
import com.example.bucketlist.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;
import java.util.Map;

public class UserDetail extends AppCompatActivity implements View.OnClickListener {

    String email,phoneNumber;
    RelativeLayout profileLayout;
    ImageView profileImage,addButton;
    Button completeButton;
    EditText displayNameText;
    String name,user_id, StringImageUri;
    Uri imageUri ;

    ProgressDialog progressDialog;
    Dialog dialog;

    UploadTask uploadTask;
    StorageReference storageReference;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);



        firebaseAuth= FirebaseAuth.getInstance();
        user_id = firebaseAuth.getCurrentUser().getUid();
        email = firebaseAuth.getCurrentUser().getEmail();
        phoneNumber = firebaseAuth.getCurrentUser().getPhoneNumber();
        password = getIntent().getStringExtra("password");
        Toast.makeText(this, email+ "\n"+ phoneNumber, Toast.LENGTH_SHORT).show();

        firebaseFirestore = FirebaseFirestore.getInstance();

        storageReference = FirebaseStorage.getInstance().getReference().child(user_id);

       

        displayNameText = findViewById(R.id.displayNameText);
        completeButton = findViewById(R.id.completeButton);
        profileLayout = findViewById(R.id.profileLayout);
        profileImage = findViewById(R.id.profileImage);
        addButton = findViewById(R.id.addImageButton);

        progressDialog = new ProgressDialog(this);
         dialog = new Dialog(this);

        completeButton.setOnClickListener(this);
        profileLayout.setOnClickListener(this);






    }//end of on create

    @Override
    public void onClick(View v) {


        if (v.getId() == R.id.completeButton){
            name = displayNameText.getText().toString();
            if(name.isEmpty()){
                displayNameText.setError("Please enter name");
            }
            else{
                progressDialog.setMessage("Completing setup...");
                progressDialog.show();
                DocumentReference documentReference = firebaseFirestore.collection("Users").document(user_id);
                Map<String,Object> user = new HashMap<>();
                user.put("Display Name",name);
                user.put("Phone Number",phoneNumber);
                user.put("Email Address",email);
                user.put("Image Uri",StringImageUri);
                user.put("User password",password);
                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(UserDetail.this, "firestore Updated", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UserDetail.this, "firestore Updated failed", Toast.LENGTH_SHORT).show();
                    }
                });
                progressDialog.dismiss();
                Intent intent =new Intent(UserDetail.this, HomeActivity.class);
                startActivity(intent);


            }
        }

        else if (v.getId() == R.id.profileLayout){

            CropImage.activity().start(UserDetail.this);



        }

    }//end of onclick

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == Activity.RESULT_OK) {
                 imageUri = result.getUri();
                 profileImage.setImageURI(imageUri);
                 uploadImageToFirebase(imageUri);
                 addButton.setVisibility(View.INVISIBLE);

            }
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Exception e = result.getError();
                Toast.makeText(UserDetail.this, "error "+ e, Toast.LENGTH_SHORT).show();
            }
        }
    }//end of onActivityResult



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
                                StringImageUri = uri.toString();
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




}//end of class