package com.example.bucketlist;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bucketlist.adapters.PostRecyclerAdapter;
import com.example.bucketlist.model.ActivityModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Bookmark extends AppCompatActivity {

    FirebaseFirestore firestore;
    FirebaseAuth auth;
    RecyclerView bookmarkRecyclerview;
    PostRecyclerAdapter bookmarkrecyclerAdapter;
    List<ActivityModel> modelList;




    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bookmark);

        bookmarkRecyclerview = findViewById(R.id.bookmarkRecyclerview);


        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        bookmarkRecyclerview.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        bookmarkRecyclerview.setItemAnimator(new DefaultItemAnimator());

        LoadBookamrkPost();

    }

    private void LoadBookamrkPost() {
        modelList = new ArrayList<>();
        CollectionReference collection = firestore.collection("Users").document(auth.getCurrentUser().getUid())
                .collection("Bookmarks");
        collection.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error!=null){
                    error.printStackTrace();
                }
                else{
                    for (QueryDocumentSnapshot snapshot : value){
                        snapshot.getDocumentReference("post reference").addSnapshotListener(new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                              if (error!=null){
                                  error.printStackTrace();
                              }
                              else{
                                  modelList.add(new ActivityModel(value.getString("createdBy"),
                                          value.getString("title"),
                                          value.getLong("timeStamp").longValue(),
                                          value.getString("location"),
                                          value.getLong("likes").intValue(),
                                          value.getId()));
                                  bookmarkrecyclerAdapter.notifyDataSetChanged();
//
                                      Log.d("title",value.getDocumentReference("post reference").toString());
                              }
                            }
                        });
                    }
                }
            }
        });

        bookmarkrecyclerAdapter = new PostRecyclerAdapter(getApplicationContext(),modelList);
        bookmarkRecyclerview.setAdapter(bookmarkrecyclerAdapter);
    }

}
