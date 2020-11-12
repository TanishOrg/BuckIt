package com.example.bucketlist;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.example.bucketlist.adapters.RecyclerAdapterComment;
import com.example.bucketlist.adapters.RecyclerAdapterTrendingCard;
import com.example.bucketlist.layout.loginLayouts.OtpActivityRegister;
import com.example.bucketlist.layout.userLayout.UserDetail;
import com.example.bucketlist.model.CommentModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
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

public class PostInnerPage extends AppCompatActivity implements View.OnClickListener, Toolbar.OnMenuItemClickListener {
    private static final String TAG = "Post Inner Page";
    String postId;
    String location,username,title,description;
    BottomSheetDialog bottomSheetDialog;
    String dateAsText;
    int likes = 0;
    int dislikes = 0;
    boolean isdisliked = false;
    boolean isliked = false;
    Long timestamp;
    FirebaseFirestore firestore;
    Toolbar toolbar;
    int totalComments;
    CircleImageView userImage;
    RecyclerView commentRecyclerView;
    List<CommentModel> commentModelList ;
    RecyclerAdapterComment adapterComment;
    SwipeRefreshLayout refreshLayout;

    String toactivity;
    int intpoints = 0;
    FirebaseAuth auth;

    EditText commentText;
    TextView comment;
    ImageView backButton,bookmarkButton,sendCommentButton;
    ImageView likeButton;
    ImageView dislikeButton;
    TextView locationView,createdBy,timeCreated,titleView,descriptionView,points
            ,noOfComments,toolbartitle;

    boolean isbookmarked=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.post_inner_page_activity);

        toolbar = findViewById(R.id.toolBar);

        toactivity = getIntent().getStringExtra("to activity");
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
        points = findViewById(R.id.points);
        backButton = findViewById(R.id.backButton);
        userImage = findViewById(R.id.userImage);
        bookmarkButton = findViewById(R.id.saveBookmark);
        noOfComments = findViewById(R.id.noOfComments);
        sendCommentButton = findViewById(R.id.sendCommentButton);
        commentText = findViewById(R.id.commentText);
        comment=findViewById(R.id.comment);
        likeButton = findViewById(R.id.likeButton);
        dislikeButton = findViewById(R.id.dislikeButton);
        commentRecyclerView = findViewById(R.id.commentRecyclerView);
        toolbartitle = findViewById(R.id.toolbartitle);
        refreshLayout = findViewById(R.id.refreshLayout);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(false);
                boolean connection=isNetworkAvailable();
                if(connection){
                    finish();
                    startActivity(getIntent());
                }
                else{
                    Toast.makeText(PostInnerPage.this, "Internet connection not available", Toast.LENGTH_SHORT).show();
                }
            }
        });
        refreshLayout.setColorSchemeColors(Color.RED);

        likeButton.setOnClickListener(this);
        dislikeButton.setOnClickListener(this);
        backButton.setOnClickListener(this);
        bookmarkButton.setOnClickListener(this);
        sendCommentButton.setOnClickListener(this);

        bottomSheetDialog = new BottomSheetDialog(this);

        if (postId!=null){
            loadPost();
            loadComments();
        }
        ifDocExists();
        ifBookmarkExists();

        toolbar.setOnMenuItemClickListener(this);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager=(ConnectivityManager) this.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        return networkInfo !=null;
    }

    private void ifDocExists() {

        firestore.collection("Posts").document(postId)
                .collection("LikedBy").document(auth.getCurrentUser().getUid())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (value.exists()) {

                            DrawableCompat.setTint(likeButton.getDrawable(), ContextCompat.getColor(getApplicationContext(), R.color.selectediconcolor));
                            DrawableCompat.setTint(dislikeButton.getDrawable(), ContextCompat.getColor(getApplicationContext(),R.color.postAction));
                            isliked = true;

                        }
                    }
                });
        firestore.collection("Posts").document(postId)
                .collection("DislikedBy").document(auth.getCurrentUser().getUid())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (value.exists()) {
                            DrawableCompat.setTint(dislikeButton.getDrawable(), ContextCompat.getColor(getApplicationContext(), R.color.selectediconcolor));
                            DrawableCompat.setTint(likeButton.getDrawable(), ContextCompat.getColor(getApplicationContext(),R.color.postAction));
                            isdisliked = true;
                        }
                    }
                });

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
                            commentModelList.clear();
                            for (QueryDocumentSnapshot snapshot:value){
                                commentModelList.add(
                                        new CommentModel(snapshot.getString("user id")
                                        ,snapshot.getString("comment")
                                        ,snapshot.getLong("time of comment").longValue(),
                                        snapshot.getLong("total likes").intValue(),
                                                snapshot.getId())

                                );


                            }adapterComment.notifyDataSetChanged();
                        }
                    }
                });
        adapterComment= new RecyclerAdapterComment(getApplicationContext(),commentModelList,postId,bottomSheetDialog);
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
                else if (value.exists()){
                    location =  value.getString("location");
                    String[] arr = location.split(", ",0);
                    String cityFilename = arr[0] + ", " + arr[arr.length - 1];
                    locationView.setText(cityFilename);
                    toolbartitle.setText(cityFilename);


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
                    dislikes = value.getLong("dislikes").intValue();

                    intpoints = likes-dislikes;
                    points.setText(Integer.toString(intpoints)+" Votes");

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

                 if (auth.getCurrentUser().getUid().equals(username)){
                     toolbar.inflateMenu(R.menu.menu_post_mine);
                 }
                 else{
                     toolbar.inflateMenu(R.menu.menu_post_other);
                 }

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
    private void ifBookmarkExists() {
        firestore.collection("Users").document(auth.getCurrentUser().getUid())
                .collection("Bookmarks").document(postId)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (value.exists()) {

                            bookmarkButton.setImageResource(R.drawable.ic_baseline_bookmark_24);
                            isbookmarked = true;

                        }else {
                            bookmarkButton.setImageResource(R.drawable.ic_baseline_bookmark_border_24);
                            isbookmarked=false;
                        }
                    }
                });

    }

    public void bookmarking(){
        if(isbookmarked==false){
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

//            DrawableCompat.setTint(bookmarkButton.getDrawable(), ContextCompat.getColor(getApplicationContext(),R.color.colorwhite));
            bookmarkButton.setImageResource(R.drawable.ic_baseline_bookmark_24);
            isbookmarked=true;
        }else{
             firestore.collection("Users").document(auth.getCurrentUser().getUid())
                    .collection("Bookmarks").document(postId).delete()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                     @Override
                     public void onComplete(@NonNull Task<Void> task) {
                         if(task.isSuccessful()){
                             Log.d(TAG, "onComplete: " + "bookmarked");
                             Toast.makeText(PostInnerPage.this, "Bookmark Removed", Toast.LENGTH_SHORT).show();
                         }

                     }
                 });

            bookmarkButton.setImageResource(R.drawable.ic_baseline_bookmark_border_24);
            isbookmarked=false;
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.backButton:
                finish();
//
//                try {
//                    if (toactivity!=null){
////                        backtowhichactivity();
//                    }
//                }catch (Exception e){
//                    e.printStackTrace();
//                }

                break;
            case R.id.saveBookmark:
                bookmarking();
                break;
            case R.id.likeButton:
                like();
                break;
            case R.id.dislikeButton:
                dislike();
                Log.d(TAG, "onClick: ");
                break;
            case R.id.sendCommentButton:
                storingcomment();


        }
    }

    private void storingcomment() {
        if (!commentText.getText().toString().isEmpty()){
            totalComments++;
            long timecreated = System.currentTimeMillis();
            DocumentReference commentDocRef = firestore.collection("Posts").document(postId)
                    .collection("Comments").document(Long.toString(timecreated));

            Map commentmap = new HashMap();
            commentmap.put("user id",auth.getCurrentUser().getUid());
            commentmap.put("comment",commentText.getText().toString());
            commentmap.put("time of comment",timecreated);
            commentmap.put("total likes",0);

            commentDocRef.set(commentmap).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d("msg","comment added");
                    firestore.collection("Posts").document(postId).update("total comments",totalComments);
                    commentText.setText(null);

                }
            });
        }



    }

    private void like() {
        if (isliked==false){
            if (isdisliked==true){
                firestore.collection("Posts").document(postId)
                        .collection("DislikedBy").document(auth.getCurrentUser().getUid()).delete();
                dislikes--;
                isdisliked = false;
            }


            CollectionReference collectionReference = firestore.collection("Posts").document(postId)
                    .collection("LikedBy");
            Map map1 = new HashMap();
            map1.put("ref", firestore.collection("Users").document(auth.getCurrentUser().getUid()));
            collectionReference.document(auth.getCurrentUser().getUid()).set(map1).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "onComplete: " + "liked");
//                    Drawable d = getResources().getDrawable(R.drawable.up_arrow_icon);
//                    d.setColorFilter( 0xffff0000, PorterDuff.Mode.MULTIPLY );
                        DrawableCompat.setTint(likeButton.getDrawable(), ContextCompat.getColor(getApplicationContext(), R.color.selectediconcolor));
                        DrawableCompat.setTint(dislikeButton.getDrawable(), ContextCompat.getColor(getApplicationContext(), R.color.postAction));

                    }
                }
            });
            isliked = true;
            likes++;

        }
        else {
            firestore.collection("Posts").document(postId)
                    .collection("LikedBy").document(auth.getCurrentUser().getUid()).delete();
            DrawableCompat.setTint(likeButton.getDrawable(), ContextCompat.getColor(getApplicationContext(), R.color.postAction));
            isliked = false;
            likes--;
        }
        Map likedislike = new HashMap();
        likedislike.put("likes",likes);
        likedislike.put("dislikes",dislikes);
        firestore.collection("Posts").document(postId).update(likedislike).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                Log.d("like dislike updated","yes");
            }
        });
    }


    private void dislike() {

        if (isdisliked == false){
            if (isliked == true){
                firestore.collection("Posts").document(postId)
                        .collection("LikedBy").document(auth.getCurrentUser().getUid()).delete();
                likes--;
                isliked = false;
            }


            CollectionReference collectionReference = firestore.collection("Posts").document(postId)
                    .collection("DislikedBy");
            Map map1 = new HashMap();
            map1.put("ref",firestore.collection("Users").document(auth.getCurrentUser().getUid()));
            collectionReference.document(auth.getCurrentUser().getUid()).set(map1).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "onComplete: " + "liked");
                        DrawableCompat.setTint(dislikeButton.getDrawable(), ContextCompat.getColor(getApplicationContext(), R.color.selectediconcolor));
                        DrawableCompat.setTint(likeButton.getDrawable(), ContextCompat.getColor(getApplicationContext(),R.color.postAction));

                    }
                }
            });
            isdisliked = true;
            dislikes++;

        }
        else {
            firestore.collection("Posts").document(postId)
                    .collection("DislikedBy").document(auth.getCurrentUser().getUid()).delete();
            DrawableCompat.setTint(dislikeButton.getDrawable(), ContextCompat.getColor(getApplicationContext(), R.color.postAction));
            isdisliked = false;
            dislikes--;
        }
        Map likedislike = new HashMap();
        likedislike.put("likes",likes);
        likedislike.put("dislikes",dislikes);
        firestore.collection("Posts").document(postId).update(likedislike).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                Log.d("like dislike updated","yes");
            }
        });
    }

    public void backtowhichactivity(){
        Intent in = null;
        
        
        switch (toactivity){
            case "city fragment":
               in = new Intent(this, HomeActivity.class);
                in.putExtra("which Activity","from Add new city");
                break;
            case "city inner page":
                in = new Intent(this, CityInnerPage.class);
                in.putExtra("cityId",location);
                break;
            case "bookmark page":
                new Intent(this, BookmarkPage.class);
                break;
            case "my post page":
                new Intent(this, myPost.class);
                break;
            case "see more post page":
                new Intent(this, SeemorePosts.class);
                break;
            default:
                in = new Intent(this, HomeActivity.class);
                in.putExtra("which Activity","from Add new city");
                break;
        }

        finish();
        startActivity(in);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.edit:
                Intent intent = new Intent(this,EditPostPage.class);
                intent.putExtra("postid",postId);
                startActivity(intent);
                break;
            case R.id.report:

                Toast.makeText(this, "Reported", Toast.LENGTH_SHORT).show();
                break;
            case R.id.delete:

                final AlertDialog.Builder alert;
                alert = new AlertDialog.Builder(PostInnerPage.this);
                View view1 = getLayoutInflater().inflate(R.layout.delete_confirmation,null);

                final TextView confirmdeletionButton = view1.findViewById(R.id.confirmDeletionButton);
                final TextView canceldeletinButton = view1.findViewById(R.id.cancelDeletionButton);
                alert.setView(view1);

                final AlertDialog alertDialog = alert.create();
                alertDialog.setCanceledOnTouchOutside(false);
                canceldeletinButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });
                confirmdeletionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        firestore.collection("Posts").document(postId)
                                .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                final DocumentReference ref = firestore.collection("Users").document(auth.getCurrentUser().getUid())
                                        .collection("Bookmarks").document(postId);
                                ref .get()
                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    DocumentSnapshot  val = task.getResult();
                                                    if(val.exists()) {
                                                        ref.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {

                                                            }
                                                        });
                                                    }
                                                }
                                            }
                                        });
                                Toast.makeText(PostInnerPage.this, "succesful", Toast.LENGTH_SHORT).show();
                                finish();
                                alertDialog.dismiss();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
                    }
                });
                alertDialog.show();


                break;

        }
        return false;
    }
}