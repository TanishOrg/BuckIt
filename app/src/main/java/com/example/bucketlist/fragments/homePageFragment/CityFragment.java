package com.example.bucketlist.fragments.homePageFragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.bucketlist.AddNewCity;
import com.example.bucketlist.AddNewPost;
import com.example.bucketlist.R;
import com.example.bucketlist.SeeMoreCities;
import com.example.bucketlist.SeemorePosts;
import com.example.bucketlist.adapters.PostRecyclerAdapter;
import com.example.bucketlist.adapters.RecyclerAdapterTrendingCard;
import com.example.bucketlist.adapters.PageAdapterTrendingCard;
import com.example.bucketlist.model.ActivityModel;
import com.example.bucketlist.model.CityModel;
import com.example.bucketlist.model.TrendingCardModel;
import com.example.bucketlist.myPost;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.vansuita.gaussianblur.GaussianBlur;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class CityFragment extends Fragment implements View.OnClickListener {

    private View view;

    RecyclerView recyclerView,postRecyclerView;
    ViewPager viewPager;
    FirebaseFirestore firestore;
    FirebaseAuth firebaseAuth;
    private PageAdapterTrendingCard pageAdapterTrendingCard;
    private List<TrendingCardModel> trendingCardModelList;
    TextView createCity;
    ImageView activity;
    TextView seemore,postMore;

    Toolbar toolbar;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    Context context;

    String user_id;
    String stringImageUri;

    ImageView profileImage;
    TextView profileName;


    int currentPage = 0;
    Timer timer;
    final long DELAY_MS = 500;
    final long PERIOD_MS = 3000;

    RecyclerAdapterTrendingCard recyclerAdapterTrendingCard;
    PostRecyclerAdapter postRecyclerAdapter;
    List<CityModel> arrayList;
    List<ActivityModel> List;



    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view= inflater.inflate(R.layout.fragment_city,container,false);
        createCity = view.findViewById(R.id.city);
        activity = view.findViewById(R.id.activity);
        viewPager = view.findViewById(R.id.viewPager);
        seemore = view.findViewById(R.id.seemore);
        postMore = view.findViewById(R.id.postMore);

        toolbar = view.findViewById(R.id.toolBar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        drawerLayout = view.findViewById(R.id.drawer);
        navigationView = view.findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.myPost:
                        Intent i = new Intent(getContext(), myPost.class);
                        startActivity(i);
                    case R.id.bookmark:

                }
                return false;
            }
        });


        actionBarDrawerToggle = new ActionBarDrawerToggle(this.getActivity(),drawerLayout,toolbar,R.string.draw_open,R.string.drawer_closed);
        actionBarDrawerToggle.syncState();
        actionBarDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.blackcolor));

        View headerView = navigationView.getHeaderView(0);
        ImageView cancelButton = headerView.findViewById(R.id.cancel_button);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });


        firestore =FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        context = getActivity().getApplicationContext();

        activity.setOnClickListener(this);
        createCity.setOnClickListener(this);
        seemore.setOnClickListener(this);
//        postMore.setOnClickListener(this);
        recyclerView =  view.findViewById(R.id.recycler_view);

        postRecyclerView=view.findViewById(R.id.recycler_view_post);

        profileImage = headerView.findViewById(R.id.profileImage1);
        profileName = headerView.findViewById(R.id.profileName1);
        user_id = firebaseAuth.getCurrentUser().getUid();


        trendingCardDataLoading();

        PostLoading();

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        trendingbottomCardDataLoading();

        autoScroll();

        loadData(user_id);

        return  view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.city){
            Intent i = new Intent(getContext(), AddNewCity.class);
            startActivity(i);
        }

        else if (v.getId() == R.id.activity){
            Intent i = new Intent(getContext(), AddNewPost.class);
            startActivity(i);
        }

        else if (v.getId() == R.id.seemore){
            Intent i = new Intent(getContext(), SeeMoreCities.class);
            startActivity(i);
        }
        else if (v.getId() == R.id.postMore){
            Intent i = new Intent(getContext(), SeemorePosts.class);
            startActivity(i);
        }
    }

    public void trendingCardDataLoading(){


        trendingCardModelList = new ArrayList<>();

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
                                    trendingCardModelList.add(new TrendingCardModel(value.getString("City Name"),
                                            value.getString("Country Name"),
                                            value.getString("City Background Image"),snapshot.getId()));
                                    pageAdapterTrendingCard.notifyDataSetChanged();
                                    Log.d("size",Integer.toString(trendingCardModelList.size()));
                                }

                            }
                        });

                    }

                }
            }
        });



        pageAdapterTrendingCard = new PageAdapterTrendingCard(getContext(),trendingCardModelList);
        viewPager.setAdapter(pageAdapterTrendingCard);
        viewPager.setPadding(150,0,150,0);

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
                                    recyclerAdapterTrendingCard.notifyDataSetChanged();
                                }

                            }
                        });

                    }


                }
            }
        });

        recyclerAdapterTrendingCard = new RecyclerAdapterTrendingCard(getContext(),arrayList);
        recyclerView.setAdapter(recyclerAdapterTrendingCard);
//        viewPager.setPadding(50,0,50,0);

    }



    public void autoScroll(){

        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {

                if (viewPager.getCurrentItem() == trendingCardModelList.size()-1) {
                    currentPage=0;
                }

//                viewPager.setCurrentItem(currentPage, true);
//                currentPage++;
            }
        };

        timer = new Timer(); // This will create a new Thread
        timer.schedule(new TimerTask() { // task to be scheduled
            @Override
            public void run() {

                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);
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
                                           value.getId()));
                                    postRecyclerAdapter.notifyDataSetChanged();
                                }

                            }
                        });

                    }


                }
            }
        });

        postRecyclerAdapter = new PostRecyclerAdapter(getContext(),List);
        postRecyclerView.setAdapter(postRecyclerAdapter);


    }
    private void loadData(String user_id) {
        DocumentReference documentReference = firestore.collection("Users").document(user_id);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error!=null){
                    Log.d("City Fragment error","Error:"+error.getMessage());
                }
                else {
                    profileName.setText(value.getString("Display Name"));
                    stringImageUri = value.getString("Image Uri");
                    Glide.with(context).load(stringImageUri).into(profileImage);

                }
            }
        });
    }

}
