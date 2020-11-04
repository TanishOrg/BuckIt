package com.example.bucketlist;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bucketlist.adapters.RecyclerAdapterComment;
import com.example.bucketlist.adapters.RecyclerAdapterTrendingCard;
import com.example.bucketlist.model.CommentModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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
    String postId;
    String location,username,title,description;
    String dateAsText;
    int likes;
    Long timestamp;
    FirebaseFirestore firestore;
    Toolbar toolbar;
    int totalComments;
    CircleImageView userImage;
    RecyclerView commentRecyclerView;
    List<CommentModel> commentModelList ;
    RecyclerAdapterComment adapterComment;

    FirebaseAuth auth;

    EditText commentText;
    ImageView backButton,bookmarkButton,sendCommentButton;

    TextView locationView,createdBy,timeCreated,titleView,descriptionView,likesView
            ,noOfComments;

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
        noOfComments = findViewById(R.id.noOfComments);
        sendCommentButton = findViewById(R.id.sendCommentButton);
        commentText = findViewById(R.id.commentText);
        commentRecyclerView = findViewById(R.id.commentRecyclerView);


        backButton.setOnClickListener(this);
        bookmarkButton.setOnClickListener(this);
        sendCommentButton.setOnClickListener(this);

        if (postId!=null){
            loadPost();
            loadComments();
        }
    }

    private void loadComments() {
        commentModelList = new ArrayList<>();
        firestore.collection("Posts").document(postId).collection("Comments")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error!=null){
                            error.printStackTrace();
                        }
                        else{
                            for (QueryDocumentSnapshot snapshot:value){
                                commentModelList.add(new CommentModel(snapshot.getString("user id")
                                        ,snapshot.getString("comment")
                                        ,snapshot.getLong("time of comment").longValue(),
                                        snapshot.getLong("total likes").intValue()));


                            }adapterComment.notifyDataSetChanged();
                        }
                    }
                });
        adapterComment= new RecyclerAdapterComment(getApplicationContext(),commentModelList);
        commentRecyclerView.setAdapter(adapterComment);



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

                    totalComments = value.getLong("total comments").intValue();
                    noOfComments.setText(Integer.toString(totalComments) +" Comments");



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




        Log.d("no of comment",Integer.toString(totalComments));

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
            case R.id.sendCommentButton:
              storingcomment();


        }
    }

    private void storingcomment() {
        if (!commentText.getText().toString().isEmpty()){
            totalComments++;
            DocumentReference commentDocRef = firestore.collection("Posts").document(postId)
                    .collection("Comments").document("comment"+Integer.toString(totalComments));

            Map commentmap = new HashMap();
            commentmap.put("user id",auth.getCurrentUser().getUid());
            commentmap.put("comment",commentText.getText().toString());
            commentmap.put("time of comment",System.currentTimeMillis());
            commentmap.put("total likes",0);

            commentDocRef.set(commentmap).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d("msg","comment added");
                    firestore.collection("Posts").document(postId).update("total comments",totalComments);
                    commentText.setText(null);

                    finish();
                    startActivity(getIntent());
                }
            });
        }



    }


}