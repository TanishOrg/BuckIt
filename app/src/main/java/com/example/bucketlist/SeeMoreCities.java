package com.example.bucketlist;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bucketlist.adapters.RecyclerAdapterSeemoreCities;
import com.example.bucketlist.adapters.RecyclerAdapterTrendingCard;
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

public class SeeMoreCities extends AppCompatActivity implements View.OnClickListener {
    RecyclerAdapterSeemoreCities recyclerAdapterSeemoreCities;
    List<CityModel> arrayList;
    RecyclerView recyclerView;
    FirebaseFirestore firestore;
    ImageView backButton;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.see_more_cities);
        recyclerView = findViewById(R.id.recyclerView);
        backButton = findViewById(R.id.backButton);
        firestore = FirebaseFirestore.getInstance();

        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        trendingbottomCardDataLoading();

        backButton.setOnClickListener(this);


    }

    public void trendingbottomCardDataLoading(){

        arrayList = new ArrayList<>();

        CollectionReference collectionReference = firestore.collection("Cities");
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error!=null){
                    Log.d("Exception Failed", "onEvent: 0  " + error);

                }
                else{
                    for (final QueryDocumentSnapshot snapshot : value){
                        DocumentReference documentReference = firestore.collection("Cities").document(snapshot.getId());
                        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                                if (error!=null){
                                    Log.d("Exception Failed", "onEvent: 0  " + error);

                                }
                                else{
                                    arrayList.add(new CityModel(value.getString("City Background Image"),
                                            value.getString("City Name"),
                                            value.getString("Country Name"),snapshot.getId()));
                                    recyclerAdapterSeemoreCities.notifyDataSetChanged();
                                }

                            }
                        });

                    }
                }
            }
        });

        recyclerAdapterSeemoreCities = new RecyclerAdapterSeemoreCities(this,arrayList);
        recyclerView.setAdapter(recyclerAdapterSeemoreCities);


    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.backButton){
            finish();

        }
    }
}
