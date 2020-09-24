package com.example.bucketlist;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;
import java.util.Map;

public class UserDetail extends AppCompatActivity implements View.OnClickListener {

    String email,phoneNumber;
    RelativeLayout profileLayout;
    ImageView profileImage,addButton;
    Button completeButton;
    EditText displayNameText;
    String name,user_id;
    Uri imageUri ;

    ProgressDialog progressDialog;
    Dialog dialog;

    UploadTask uploadTask;
    StorageReference storageReference;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);



        firebaseAuth= FirebaseAuth.getInstance();
        user_id = firebaseAuth.getCurrentUser().getUid();
        email = firebaseAuth.getCurrentUser().getEmail();
        phoneNumber = firebaseAuth.getCurrentUser().getPhoneNumber();
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

                if(imageUri!=null){
                    uploadImageToFirebase(imageUri);
                }
                uploadImageToFirebase(imageUri);
                progressDialog.setMessage("Completing setup...");
                progressDialog.show();
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
                Picasso.get().load(imageUri).into(profileImage);
               addButton.setVisibility(View.INVISIBLE);


            }
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Exception e = result.getError();
                Toast.makeText(UserDetail.this, "error "+ e, Toast.LENGTH_SHORT).show();
            }
        }
    }//end of onActivityResult

    private String getFileExt(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImageToFirebase(Uri imageUri) {
                    //upload image to firebase storage
        final StorageReference fileRef  = storageReference.child(getFileExt(imageUri));

               uploadTask = fileRef.putFile(imageUri);
        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful())
                    throw task.getException();

                return fileRef.getDownloadUrl();
            }

        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                uploadToFirestore(task);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }//uploadImageToFirebase

    public void uploadToFirestore(Task<Uri> task){
        if (task.isSuccessful()){
            Uri downloadUri = task.getResult();
            DocumentReference documentReference = firebaseFirestore.collection("Users").document(user_id);
            Map<String,Object> user = new HashMap<>();
            user.put("Display Name",name);
            user.put("Phone Number",phoneNumber);
            user.put("Email Address",email);
            user.put("Image Uri",downloadUri.toString());
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
        }
    }




}//end of class