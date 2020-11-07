package com.example.bucketlist;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.bucketlist.adapters.PostRecyclerAdapter;
import com.example.bucketlist.fragments.homePageFragment.CityFragment;
import com.example.bucketlist.model.ActivityModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class BookmarkPage extends AppCompatActivity implements View.OnClickListener {

    FirebaseFirestore firestore;
    FirebaseAuth auth;
    RecyclerView bookmarkRecyclerview;
    PostRecyclerAdapter bookmarkrecyclerAdapter;

    ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bookmark_page);

        auth = FirebaseAuth.getInstance();
        bookmarkRecyclerview = findViewById(R.id.bookmarkRecyclerview);
        backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener(this);


        LoadBookamrkPost();
    }

    private void LoadBookamrkPost() {
        final List<ActivityModel> modelList = new ArrayList<>();
        firestore = FirebaseFirestore.getInstance();
        CollectionReference collection = firestore.collection("Users").document(auth.getCurrentUser().getUid())
                .collection("Bookmarks");
        collection.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error!=null){
                    error.printStackTrace();
                }
                else{
                    modelList.clear();
                    for (QueryDocumentSnapshot snapshot : value){


                        snapshot.getDocumentReference("post reference")
                                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                                        if (value.exists()) {
                                                modelList.add(new ActivityModel(value.getString("createdBy"),
                                                        value.getString("title"),
                                                        value.getLong("timeStamp").longValue(),
                                                        value.getString("location"),
                                                        value.getLong("likes").intValue(),
                                                        value.getLong("dislikes").intValue(),
                                                        value.getId(), value.getLong("total comments").intValue()));
                                                Log.d("title", value.getString("title"));
                                                bookmarkrecyclerAdapter.notifyDataSetChanged();

                                        }
                                    }
                                });


                    }
                }
            }
        });

        bookmarkrecyclerAdapter = new PostRecyclerAdapter(getApplicationContext(),modelList,"bookmark page");
        bookmarkRecyclerview.setAdapter(bookmarkrecyclerAdapter);
    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.backButton){
            Intent i = new Intent(getApplicationContext(), HomeActivity.class);
            i.putExtra("which Activity","from Add new city");
            startActivity(i);
        }
    }
}