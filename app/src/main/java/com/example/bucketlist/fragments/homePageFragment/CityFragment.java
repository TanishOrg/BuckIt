package com.example.bucketlist.fragments.homePageFragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

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
import java.util.Timer;
import java.util.TimerTask;


public class CityFragment extends Fragment implements View.OnClickListener {

    private View view;

    RecyclerView recyclerView,postRecyclerView;
    ViewPager viewPager;
    FirebaseFirestore firestore;
    private PageAdapterTrendingCard pageAdapterTrendingCard;
    private List<TrendingCardModel> trendingCardModelList;
    TextView createCity;
    ImageView activity;
    TextView seemore,postMore;




    int currentPage = 0;
    Timer timer;
    final long DELAY_MS = 500;
    final long PERIOD_MS = 3000;

    RecyclerAdapterTrendingCard recyclerAdapterTrendingCard;
    PostRecyclerAdapter postRecyclerAdapter;
    List<CityModel> arrayList;
    List<ActivityModel> list;
//    int images[] = {R.drawable.athens,R.drawable.colombo,R.drawable.london,R.drawable.pisa};
//    String cityName[] = {"Athens", "Colombo", "London", "Pisa"};


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view= inflater.inflate(R.layout.fragment_city,container,false);
        createCity = view.findViewById(R.id.city);
        activity = view.findViewById(R.id.activity);
        viewPager = view.findViewById(R.id.viewPager);
        seemore = view.findViewById(R.id.seemore);
        postMore = view.findViewById(R.id.postMore);


        firestore =FirebaseFirestore.getInstance();

        activity.setOnClickListener(this);
        createCity.setOnClickListener(this);
        seemore.setOnClickListener(this);
        postMore.setOnClickListener(this);
        recyclerView =  view.findViewById(R.id.recycler_view);

        postRecyclerView=view.findViewById(R.id.recycler_view_post);

        arrayList = new ArrayList<>();
        trendingCardModelList = new ArrayList<>();
        list = new ArrayList<>();

        pageAdapterTrendingCard = new PageAdapterTrendingCard(getContext(),trendingCardModelList);
        viewPager.setAdapter(pageAdapterTrendingCard);
        viewPager.setPadding(150,0,150,0);

        recyclerAdapterTrendingCard = new RecyclerAdapterTrendingCard(getContext(),arrayList);
        recyclerView.setAdapter(recyclerAdapterTrendingCard);

        postRecyclerAdapter = new PostRecyclerAdapter(getContext(),list);
        postRecyclerView.setAdapter(postRecyclerAdapter);

        trendingCardDataLoading();



        PostLoading();

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        trendingbottomCardDataLoading();

        autoScroll();




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




        CollectionReference collectionReference = firestore.collection("Cities");
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error!=null){
                    Log.d("Exception Failed", "onEvent: 0  " + error);

                }
                else{
                    for (QueryDocumentSnapshot snapshot : value) {
                        trendingCardModelList.add(new TrendingCardModel(snapshot.getString("City Name"),
                                snapshot.getString("Country Name"),
                                snapshot.getString("City Background Image"),snapshot.getId()));
                    }

                    pageAdapterTrendingCard.notifyDataSetChanged();

                    Log.d("size",Integer.toString(trendingCardModelList.size()));
//                    pageAdapterTrendingCard.notifyDataSetChanged();
//                    for (final QueryDocumentSnapshot snapshot : value){
//                        DocumentReference documentReference = firestore.collection("Cities").document(snapshot.getId());
//
//                        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
//                            @Override
//                            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
//                                if (error!=null){
//                                    Log.d("Exception Failed", "onEvent: 0  " + error);
//
//                                }
//                                else{
//                                    trendingCardModelList.add(new TrendingCardModel(value.getString("City Name"),
//                                            value.getString("Country Name"),
//                                            value.getString("City Background Image"),snapshot.getId()));
//                                    pageAdapterTrendingCard.notifyDataSetChanged();
//                                    Log.d("size",Integer.toString(trendingCardModelList.size()));
//                                }
//
//                            }
//                        });
//
//                    }

                }
            }
        });
    }

    public void trendingbottomCardDataLoading(){



        CollectionReference collectionReference = firestore.collection("Cities");
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error!=null){
                    Log.d("Exception Failed", "onEvent: 0  " + error);

                }
                else{
                    arrayList.clear();
                    for (final QueryDocumentSnapshot snapshot : value){

                        arrayList.add(new CityModel(snapshot.getString("City Background Image"),
                                snapshot.getString("City Name"),
                                snapshot.getString("Country Name"),snapshot.getId()));
//                        DocumentReference documentReference = firestore.collection("Cities").document(snapshot.getId());
//                        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
//                            @Override
//                            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
//                                if (error!=null){
//                                    Log.d("Exception Failed", "onEvent: 0  " + error);
//
//                                }
//                                else{
//                                    arrayList.add(new CityModel(snapshot.getString("City Background Image"),
//                                            snapshot.getString("City Name"),
//                                            snapshot.getString("Country Name"),snapshot.getId()));
//                                    recyclerAdapterTrendingCard.notifyDataSetChanged();
//                                }
//
//                            }
//                        });

                    }
                    recyclerAdapterTrendingCard.notifyDataSetChanged();

                }
            }
        });


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



        CollectionReference collectionReference = firestore.collection("Posts");
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error!=null){
                    Log.d("Exception Failed", "onEvent: 0  " + error);

                }
                else{
                    for (final QueryDocumentSnapshot snapshot : value){
//                        DocumentReference documentReference = firestore.collection("Posts").document(snapshot.getId());
//                        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
//                            @Override
//                            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
//                                if (error!=null){
//                                    Log.d("Exception Failed", "onEvent: 0  " + error);
//
//                                }
//                                else{
//                                   List.add(new ActivityModel(snapshot.getString("createdBy"),
//                                           snapshot.getString("title"),
//                                           snapshot.getLong("timeStamp").longValue(),
//                                           snapshot.getString("location"),
//                                           snapshot.getLong("likes").intValue(),
//                                           snapshot.getId()));
//                                    postRecyclerAdapter.notifyDataSetChanged();
//                                }
//
//                            }
//                        });
                        list.add(new ActivityModel(snapshot.getString("createdBy"),
                                snapshot.getString("title"),
                                snapshot.getLong("timeStamp").longValue(),
                                snapshot.getString("location"),
                                snapshot.getLong("likes").intValue(),
                                snapshot.getId()));
                    }
                    postRecyclerAdapter.notifyDataSetChanged();
                }
            }
        });




    }




}
