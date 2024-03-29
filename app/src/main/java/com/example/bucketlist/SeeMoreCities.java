package com.example.bucketlist;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.bucketlist.adapters.RecyclerAdapterSeemoreCities;
import com.example.bucketlist.adapters.RecyclerAdapterTrendingCard;
import com.example.bucketlist.model.CityModel;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
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
    List arrayList;
    RecyclerView recyclerView;
    FirebaseFirestore firestore;
    Toolbar toolbar;
    private SwipeRefreshLayout refreshLayout;

    public static final String BANNER_TEST_ID = "ca-app-pub-3940256099942544/6300978111";

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
                    Toast.makeText(SeeMoreCities.this, "Internet connection not available", Toast.LENGTH_SHORT).show();
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
                        arrayList.add(new CityModel(snapshot.getString("City Background Image"),
                                snapshot.getString("City Name"),
                                snapshot.getString("Country Name"),snapshot.getId()));
                    }


                    int size = arrayList.size();
                    for (int i= size-1; i >=0; i -= 4) {
//                        Log.d("TAG", "onEvent: " + i);
                        final AdView adView = new AdView(getApplicationContext());
                        adView.setAdSize(AdSize.BANNER);
//                                adView.setAdUnitId(BANNER_AD_ID);
                        adView.setAdUnitId(BANNER_TEST_ID);
                        arrayList.add(i, adView);
                    }

                    recyclerAdapterSeemoreCities.notifyDataSetChanged();
                    for (int i = 0; i < arrayList.size(); i++) {
                        Object adView = arrayList.get(i);

                        if (adView instanceof AdView) {
                            final AdView view = (AdView) adView;
                            view.loadAd(new AdRequest.Builder().build());
                        }
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
