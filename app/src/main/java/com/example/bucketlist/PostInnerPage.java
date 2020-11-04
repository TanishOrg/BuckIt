package com.example.bucketlist;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.lang.ref.Reference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostInnerPage extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "Post Inner Page";
    String postId;
    String location,username,title,description;
    String dateAsText;
    int likes;
    Long timestamp;
    FirebaseFirestore firestore;
    Toolbar toolbar;

    CircleImageView userImage;

    FirebaseAuth auth;

    ImageView backButton,bookmarkButton;
    ImageView likeButton;
    ImageView dislikeButton;

    TextView locationView,createdBy,timeCreated,titleView,descriptionView,likesView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.post_inner_page_activity);

        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);



        postId = getIntent().getStringExtra("postId");
        initialize();



    }

    public void initialize(){
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        locationView = findViewById(R.id.location);
        createdBy = findViewById(R.id.createdBy);
        timeCreated = findViewById(R.id.timeCreated);
        titleView =findViewById(R.id.titleView);
        descriptionView  =findViewById(R.id.descriptionView);
        likesView = findViewById(R.id.noOfLikes);
        backButton = findViewById(R.id.backButton);
        userImage = findViewById(R.id.userImage);
        bookmarkButton = findViewById(R.id.saveBookmark);
        likeButton = findViewById(R.id.likeButton);
        dislikeButton = findViewById(R.id.dislikeButton);

        likeButton.setOnClickListener(this);
        dislikeButton.setOnClickListener(this);
        backButton.setOnClickListener(this);
        bookmarkButton.setOnClickListener(this);

        if (postId!=null){
            loadPost();
        }
        ifDocExists();
    }

    private void ifDocExists() {
         firestore.collection("Posts").document(postId)
                .collection("LikedBy").document(auth.getCurrentUser().getUid())
                 .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                     @Override
                     public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                         if (value.exists()) {

                             DrawableCompat.setTint(likeButton.getDrawable(), ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
                             DrawableCompat.setTint(dislikeButton.getDrawable(), ContextCompat.getColor(getApplicationContext(),R.color.postAction));

                         }
                     }
                 });
        firestore.collection("Posts").document(postId)
                .collection("DislikedBy").document(auth.getCurrentUser().getUid())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (value.exists()) {

                            DrawableCompat.setTint(dislikeButton.getDrawable(), ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
                            DrawableCompat.setTint(likeButton.getDrawable(), ContextCompat.getColor(getApplicationContext(),R.color.postAction));

                        }
                    }
                });
    }

    public void loadPost(){
        DocumentReference documentReference = firestore.collection("Posts").document(postId);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error!=null){
                    error.printStackTrace();
                }
                else {
                    location =  value.getString("location");
                    String[] arr = location.split(", ",0);
                    String cityFilename = arr[0] + ", " + arr[arr.length - 1];
                    locationView.setText(cityFilename);


                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(new Date());
                    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
                    sdf.setTimeZone(TimeZone.getDefault());
                    timestamp = value.getLong("timeStamp").longValue();
                    dateAsText = sdf.format(new Date(timestamp).getTime());
                    timeCreated.setText(dateAsText);


                    title = value.getString("title");
                    titleView.setText(title);


                    description = value.getString("description");
                    descriptionView.setText(description);


                    likes = value.getLong("likes").intValue();
                    likesView.setText(Integer.toString(likes));



                    username = value.getString("createdBy");
                    FirebaseFirestore.getInstance().collection("Users")
                            .document(username).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                            if (error!=null){
                                Log.e("error",error.getMessage());
                            }
                            else {
                                createdBy.setText(value.getString("Display Name"));
                            }
                        }
                    });

                }
            }
        });

        firestore.collection("Users").document(auth.getCurrentUser().getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error!=null){
                    error.printStackTrace();
                }
                else{
                    Glide.with(getApplicationContext()).load(value.getString("Image Uri")).into(userImage);
                }
            }
        });

    }

    public void bookmarking(){
        DocumentReference documentReference = firestore.collection("Users").document(auth.getCurrentUser().getUid())
                .collection("Bookmarks").document(postId);
        DocumentReference postDocRef = firestore.collection("Posts").document(postId);
        Map map = new HashMap();
        map.put("post reference",postDocRef);
        documentReference.set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("successful","successful");
                Toast.makeText(PostInnerPage.this, "Bookmarked", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.backButton:
                Intent i = new Intent(this, CityInnerPage.class);
                i.putExtra("cityId",location);
                finish();
                startActivity(i);
                break;
            case R.id.saveBookmark:
               bookmarking();
               break;
            case R.id.likeButton:
                like(view);
                break;
            case R.id.dislikeButton:
                dislike();
                Log.d(TAG, "onClick: ");
                break;

        }
    }

    /**
     * We have the post id and in users document we will create a new collection of liked posts
     */
    private void like(final View view) {

//        CollectionReference reference = firestore.collection("Users")
//                .document(auth.getCurrentUser().getUid()).collection("LikedPost");
//        DocumentReference documentReference = firestore.collection("Posts").document(postId);
//        Map map = new HashMap();
//        map.put("ref",documentReference);
//        reference.document(postId).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//                Snackbar.make(view,"Liked",Snackbar.LENGTH_SHORT).show();
//            }
//        });
        firestore.collection("Posts").document(postId)
                .collection("DislikedBy").document(auth.getCurrentUser().getUid()).delete();

        CollectionReference collectionReference = firestore.collection("Posts").document(postId)
                .collection("LikedBy");
        Map map1 = new HashMap();
        map1.put("ref",firestore.collection("Users").document(auth.getCurrentUser().getUid()));
        collectionReference.document(auth.getCurrentUser().getUid()).set(map1).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "onComplete: " + "liked");
//                    Drawable d = getResources().getDrawable(R.drawable.up_arrow_icon);
//                    d.setColorFilter( 0xffff0000, PorterDuff.Mode.MULTIPLY );
                    DrawableCompat.setTint(likeButton.getDrawable(), ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
                    DrawableCompat.setTint(dislikeButton.getDrawable(), ContextCompat.getColor(getApplicationContext(),R.color.postAction));

                }
            }
        });

    }

    private void dislike() {

        firestore.collection("Posts").document(postId)
                .collection("LikedBy").document(auth.getCurrentUser().getUid()).delete();

        CollectionReference collectionReference = firestore.collection("Posts").document(postId)
                .collection("DislikedBy");
        Map map1 = new HashMap();
        map1.put("ref",firestore.collection("Users").document(auth.getCurrentUser().getUid()));
        collectionReference.document(auth.getCurrentUser().getUid()).set(map1).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {
                    Log.d(TAG, "onComplete: " + "liked");
                    DrawableCompat.setTint(dislikeButton.getDrawable(), ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
                    DrawableCompat.setTint(likeButton.getDrawable(), ContextCompat.getColor(getApplicationContext(),R.color.postAction));

                }
            }
        });
    }
}