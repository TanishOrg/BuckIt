package com.example.bucketlist;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.bucketlist.adapters.PostRecyclerAdapter;
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

public class SeemorePosts extends AppCompatActivity {

    PostRecyclerAdapter postRecyclerAdapter;
    List<ActivityModel> List;
    FirebaseFirestore firestore;
    RecyclerView postRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seemore_posts_activity);
        postRecyclerView = findViewById(R.id.recyclerView);
        firestore = FirebaseFirestore.getInstance();


        PostLoading();

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
                        DocumentReference documentReference = firestore.collection("Posts").document(snapshot.getId());
                        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                                if (error!=null){
                                    Log.d("Exception Failed", "onEvent: 0  " + error);

                                }
                                else{
                                    List.add(new ActivityModel(value.getString("createdBy"),
                                            value.getString("title"),
                                            value.getLong("timeStamp").longValue(),
                                            value.getString("location"),
                                            value.getLong("likes").intValue(),
                                            value.getLong("dislikes").intValue(),
                                            value.getId(),snapshot.getLong("total comments").intValue()));
                                    postRecyclerAdapter.notifyDataSetChanged();
                                }

                            }
                        });

                    }


                }
            }
        });

        postRecyclerAdapter = new PostRecyclerAdapter(this,List,"see more post page");
        postRecyclerView.setAdapter(postRecyclerAdapter);


    }
}