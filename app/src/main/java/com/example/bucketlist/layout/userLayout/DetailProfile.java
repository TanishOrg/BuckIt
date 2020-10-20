package com.example.bucketlist.layout.userLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.bucketlist.HomeActivity;
import com.example.bucketlist.PhotoFullPopupWindow;
import com.example.bucketlist.R;
import com.example.bucketlist.layout.loginLayouts.LoginByEmailActivity;
import com.example.bucketlist.layout.openingScreen.Third_Content;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.vansuita.gaussianblur.GaussianBlur;

import java.net.URL;

public class DetailProfile extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG ="Detail Profile" ;
    private Button signoutButton,deleteButton;
    EditText displayName,emailAddress,phoneNumber;
    ImageView profileImage , editButton , backButton,profileBackground ;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    String StringImageUri,name,email,phone;
    ProgressBar progressBar2;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference =firebaseStorage.getReference().child(firebaseUser.getUid());
        setContentView(R.layout.activity_detail_profile);

        progressBar2=findViewById(R.id.progressBar2);

        //initializing view
        initialization();

        signoutButton.setOnClickListener(this);
        editButton.setOnClickListener(this);
        backButton.setOnClickListener(this);

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new PhotoFullPopupWindow(getApplicationContext(), R.layout.popup_photo_full, v, StringImageUri, null);
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(DetailProfile.this,R.style.AlertDialogTheme);
                dialog.setTitle("Are you sure?");
                dialog.setMessage("Deleting this account will result in completely removing your account from  the system");
                dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        progressBar2.setVisibility(View.VISIBLE);
                        firebaseFirestore = FirebaseFirestore.getInstance();
                        firebaseFirestore.collection("Users").document(firebaseUser.getUid()).collection("items").get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()){
                                    for (QueryDocumentSnapshot document : task.getResult()){
                                        Log.d("item deleting","first"+task.getException());
                                        firebaseFirestore.collection("Users").document(firebaseUser.getUid()).collection("items").document(document.getId()).delete();
                                    }
                                    Log.d("Message1", "Items deleted");
                                    DocumentReference docReference = firebaseFirestore.collection("Users").document(firebaseUser.getUid());
                                    docReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Log.d("Message2","user info delete"+task.getException());

                                                final StorageReference photoRef = storageReference.child("profileImage.jpeg");

                                                photoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        photoRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()){
                                                                    Log.d("delete","user profile storage");
                                                                    firebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            if(task.isSuccessful()){
                                                                                Log.d("user account","delete"+task.getException());
                                                                                progressBar2.setVisibility(View.GONE);
                                                                                Toast.makeText(DetailProfile.this,"Account Deleted",Toast.LENGTH_LONG).show();

                                                                                Intent i = new Intent(DetailProfile.this, LoginByEmailActivity.class);
                                                                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                                                startActivity(i);
                                                                            }else{
                                                                                Toast.makeText(DetailProfile.this,"user account not deleted"+task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                                                            }
                                                                        }
                                                                    }).addOnFailureListener(new OnFailureListener() {
                                                                        @Override
                                                                        public void onFailure(@NonNull Exception e) {
                                                                            Log.e(TAG,"Error in deleting account", e);
                                                                            String error = String.valueOf(e);
                                                                            Intent i = new Intent(DetailProfile.this, LoginByEmailActivity.class);
                                                                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                                            startActivity(i);
                                                                        }
                                                                    });
                                                                }
                                                                else{
                                                                    Toast.makeText(DetailProfile.this,"storage " +task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {

                                                        firebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if(task.isSuccessful()){
                                                                    Log.d("user account","delete"+task.getException());
                                                                    progressBar2.setVisibility(View.GONE);
                                                                    Toast.makeText(DetailProfile.this,"Account Deleted",Toast.LENGTH_LONG).show();

                                                                    Intent i = new Intent(DetailProfile.this, LoginByEmailActivity.class);
                                                                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                                    startActivity(i);
                                                                }else{
                                                                    Toast.makeText(DetailProfile.this,"user account not deleted"+task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                                                }
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Log.e(TAG,"Error in deleting account", e);
                                                                String error = String.valueOf(e);
                                                                Intent i = new Intent(DetailProfile.this, LoginByEmailActivity.class);
                                                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                                startActivity(i);
                                                            }
                                                        });

                                                    }
                                                });
                                            }else{
                                                Toast.makeText(DetailProfile.this,"user info not deleted"+task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                                }
                            }
                        });








                    }
                });
                dialog.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog= dialog.create();
                alertDialog.show();

            }
        });
    }




    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();
        DocumentReference documentReference = firebaseFirestore.collection("Users").document(firebaseUser.getUid());


        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                if (error!=null){
                    Log.d(TAG,"Error:"+error.getMessage());
                }
                else {
                    name = value.getString("Display Name");
                    displayName.setText(name);

                    email =  value.getString("Email Address");
                    emailAddress.setText(email);

                    phone = value.getString("Phone Number");
                    phoneNumber.setText(phone);

                    StringImageUri = value.getString("Image Uri");
                    Glide.with(getApplicationContext()).load(StringImageUri).into(profileImage);
                    Glide.with(getApplicationContext())
                            .load(StringImageUri)
                            .into(new CustomTarget<Drawable>() {
                                @Override
                                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                    GaussianBlur.with(getApplicationContext()).radius(20).put(resource,profileBackground);
                                }

                                @Override
                                public void onLoadCleared(@Nullable Drawable placeholder) {

                                }
                            });
                    Log.i(TAG, "String: "+StringImageUri);
                }

            }
        });
    }
   private void initialization(){
        signoutButton = findViewById(R.id.signoutButton);
        displayName = findViewById(R.id.displayName);
        emailAddress = findViewById(R.id.emailAddress);
        profileImage = findViewById(R.id.profileImage);
        phoneNumber = findViewById(R.id.phoneNumber);
        editButton = findViewById(R.id.editButton);
        backButton = findViewById(R.id.backButton);
        deleteButton=findViewById(R.id.deleteButton);
        profileBackground = findViewById(R.id.profileBackground);


}

   public void onClickSignoutButton(){
       if (firebaseAuth != null) {
           firebaseAuth.signOut();
           Intent i = new Intent(DetailProfile.this, LoginByEmailActivity.class);
           i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
           startActivity(i);
       }
   }

   public void onClickEditButton(){
        Intent i = new Intent(DetailProfile.this,EditProfileActivity.class);
        startActivity(i);

    }


    public void onClickBackButton(){
        Intent i = new Intent(DetailProfile.this, HomeActivity.class);
        startActivity(i);

    }


    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.signoutButton){
            onClickSignoutButton();
        }

        else if (v.getId() == R.id.editButton){
            onClickEditButton();
        }
        else if (v.getId() == R.id.backButton){
            onClickBackButton();
        }
    }




}