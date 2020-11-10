package com.example.bucketlist;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;

import com.example.bucketlist.adapters.PostRecyclerAdapter;
import com.example.bucketlist.fragments.homePageFragment.CityFragment;
import com.example.bucketlist.model.ActivityModel;
import com.example.bucketlist.model.CityModel;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SeemorePosts extends AppCompatActivity implements View.OnClickListener {

    PostRecyclerAdapter postRecyclerAdapter;
    List<ActivityModel> List;
    FirebaseFirestore firestore;
    RecyclerView postRecyclerView;
    ImageView backButton;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seemore_posts_activity);
        postRecyclerView = findViewById(R.id.recyclerView);
        backButton = findViewById(R.id.backButton);
        firestore = FirebaseFirestore.getInstance();
        searchView = findViewById(R.id.searchView);

        PostLoading();
        searchView.getBackground();
        backButton.setOnClickListener(this);
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchView.isIconified()) {
                    searchView.setBackgroundColor(Color.WHITE);
                }

            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                postRecyclerAdapter.getFilter().filter(newText);
                Log.d("TAG", "onQueryTextChange: ");
                return false;
            }
        });

    }

    public void PostLoading(){

        List = new ArrayList<>();

        CollectionReference collectionReference = firestore.collection("Posts");
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error!=null){
                    Log.d("Exception Failed", "onEvent: 0  " + error);

                }
                else{
                    for (final QueryDocumentSnapshot snapshot : value){
                        List.add(new ActivityModel(snapshot.getString("createdBy"),
                                snapshot.getString("title"),
                                snapshot.getLong("timeStamp").longValue(),
                                snapshot.getString("location"),
                                snapshot.getLong("likes").intValue(),
                                snapshot.getLong("dislikes").intValue(),
                                snapshot.getId(),snapshot.getLong("total comments").intValue()));
                        postRecyclerAdapter.notifyDataSetChanged();

                    }


                }
            }
        });

        postRecyclerAdapter = new PostRecyclerAdapter(this,List,"see more post page");
        postRecyclerView.setAdapter(postRecyclerAdapter);


    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.backButton){
            finish();

        }
    }
}