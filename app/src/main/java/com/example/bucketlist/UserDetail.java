package com.example.bucketlist;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
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
    String name,user_id,StringImageUri;
    Uri imageUri;
    ProgressDialog progressDialog;
    Dialog dialog;

    StorageReference storageReference;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        storageReference = FirebaseStorage.getInstance().getReference();

        firebaseAuth= FirebaseAuth.getInstance();
        user_id = firebaseAuth.getCurrentUser().getUid();
        email = firebaseAuth.getCurrentUser().getEmail();
        phoneNumber = firebaseAuth.getCurrentUser().getPhoneNumber();
        Toast.makeText(this, email+ "\n"+ phoneNumber, Toast.LENGTH_SHORT).show();

        firebaseFirestore = FirebaseFirestore.getInstance();


       

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
                StringImageUri = imageUri.toString();
                DocumentReference documentReference = firebaseFirestore.collection("Users").document(user_id);
                Map<String,Object> user = new HashMap<>();
                user.put("Display Name",name);
                user.put("Phone Number",phoneNumber);
                user.put("Email Address",email);
                user.put("Image Uri",StringImageUri);

                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressDialog.setMessage("Completing setup...");
                        progressDialog.show();


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UserDetail.this, "ERROR: Not able to set profile", Toast.LENGTH_SHORT).show();
                    }
                });
                Intent intent =new Intent(UserDetail.this,HomeActivity.class);
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
               addButton.setVisibility(View.INVISIBLE);
            }
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Exception e = result.getError();
                Toast.makeText(UserDetail.this, "error "+ e, Toast.LENGTH_SHORT).show();
            }
        }
    }//end of onActivityResult

//    private void uploadImageToFirebase(Uri imageUri) {
//        //upload image to firebase storage
//        StorageReference fileRef  = storageReference.child("profile.jpg");
//        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                Toast.makeText(UserDetail.this, "Profile pic set", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }//uploadImageToFirebase




}//end of class