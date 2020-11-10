package com.example.bucketlist;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;

import androidx.appcompat.widget.Toolbar;

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

public class SeeMoreCities extends AppCompatActivity {
    RecyclerAdapterSeemoreCities recyclerAdapterSeemoreCities;
    List<CityModel> arrayList;
    RecyclerView recyclerView;
    FirebaseFirestore firestore;
    Toolbar toolbar;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.see_more_cities);
        recyclerView = findViewById(R.id.recyclerView);

        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        firestore = FirebaseFirestore.getInstance();

        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        trendingbottomCardDataLoading();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.see_more_cities_menu,menu);
        MenuItem menuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) menuItem.getActionView();
       searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
           @Override
           public boolean onQueryTextSubmit(String s) {
               return false;
           }

           @Override
           public boolean onQueryTextChange(String s) {
               recyclerAdapterSeemoreCities.getFilter().filter(s);
               return false;
           }
       });
        return super.onCreateOptionsMenu(menu);
    }
}
