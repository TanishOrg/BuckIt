package com.example.bucketlist;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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

public class myPost extends AppCompatActivity implements View.OnClickListener {

    String cityId;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    List<ActivityModel> activityModelList ;
    RecyclerView myPostRecyclerView;
    PostRecyclerAdapter myPostRecyclerAdapter;
    ImageView backButton;
    private SwipeRefreshLayout refreshLayout;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypost);

        cityId = getIntent().getStringExtra("cityId");
        firestore = FirebaseFirestore.getInstance();
        backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener(this);

        auth = FirebaseAuth.getInstance();

        Log.d("created",auth.getCurrentUser().getUid());
        myPostRecyclerView = findViewById(R.id.myPostRecyclerView);



        myPostRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        myPostRecyclerView.setItemAnimator(new DefaultItemAnimator());
        PostLoading();
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
                    Toast.makeText(myPost.this, "Internet connection not available", Toast.LENGTH_SHORT).show();
                }
            }
        });
        refreshLayout.setColorSchemeColors(Color.RED);


    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager=(ConnectivityManager) this.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        return networkInfo !=null;
    }

    public void PostLoading(){

        activityModelList = new ArrayList<>();

        CollectionReference collectionReference = firestore.collection("Posts");
        Query createQuery = collectionReference.whereEqualTo("createdBy",auth.getCurrentUser().getUid());
        createQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error!=null){
                    error.printStackTrace();
                }
                else{
                    activityModelList.clear();
                    for (QueryDocumentSnapshot snapshot: value){
                        activityModelList.add(new ActivityModel(snapshot.getString("createdBy"),
                                snapshot.getString("title"),
                                snapshot.getLong("timeStamp").longValue(),
                                snapshot.getString("location"),
                                snapshot.getLong("likes").intValue(),
                                snapshot.getLong("dislikes").intValue(),
                                snapshot.getId(),snapshot.getLong("total comments").intValue()));
                        myPostRecyclerAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

        myPostRecyclerAdapter = new PostRecyclerAdapter(this,activityModelList,"my post page");
        myPostRecyclerView.setAdapter(myPostRecyclerAdapter);


    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.backButton){
            Intent i = new Intent(getApplicationContext(),HomeActivity.class);
            i.putExtra("which Activity","from Add new city");
            startActivity(i);
        }
    }
}
