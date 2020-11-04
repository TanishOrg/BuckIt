package com.example.bucketlist;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.example.bucketlist.adapters.PostRecyclerAdapter;
import com.example.bucketlist.adapters.RecyclerViewChangeWallpaper;
import com.example.bucketlist.model.ActivityModel;
import com.example.bucketlist.model.WallpaperModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CityInnerPage extends AppCompatActivity implements View.OnClickListener {
    TextView mainCity,mainState,mainCountry,mainLikes,mainVisitors;
    String cityId,imageUrl,city,state,country;
    int like,visitors;
    public  ImageView backgroundImage;
    FirebaseFirestore firestore;
    TextView changebackground;
    LinearLayout layout,confirmchangelayout;
    List<WallpaperModel> wallpaperModelList;
    RecyclerView wallpaperRecyclerView;
    CollapsingToolbarLayout collapsingToolBar;
    ImageView likebutton, doneButton,cancelButton;
    RecyclerViewChangeWallpaper recyclerViewChangeWallpaper;
    RecyclerView postRecyclerVew;
    PostRecyclerAdapter postRecyclerAdapter;

    FirebaseAuth auth;
    List<ActivityModel> activityModelList ;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.city_inner_page);
        cityId = getIntent().getStringExtra("cityId");
        firestore = FirebaseFirestore.getInstance();

        auth = FirebaseAuth.getInstance();


        mainCity = findViewById(R.id.city);
        mainCountry = findViewById(R.id.country);
        mainState = findViewById(R.id.state);
        mainLikes = findViewById(R.id.likes);
        mainVisitors = findViewById(R.id.visitors);
        backgroundImage = findViewById(R.id.backgroundImage);
        likebutton = findViewById(R.id.likebutton);
        collapsingToolBar = findViewById(R.id.collapsingToolBar);
        changebackground = findViewById(R.id.changebackground);
        layout = findViewById(R.id.layout1);
        confirmchangelayout = findViewById(R.id.confirmchangelayout);
        wallpaperRecyclerView = findViewById(R.id.wallpaperRecyclerView);
        wallpaperModelList = new ArrayList<>();
        recyclerViewChangeWallpaper = new RecyclerViewChangeWallpaper(this,wallpaperModelList,CityInnerPage.this);
        wallpaperRecyclerView.setAdapter(recyclerViewChangeWallpaper);
        doneButton = findViewById(R.id.donebutton);
        cancelButton = findViewById(R.id.cancelbutton);

//        wallpaperRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        changebackground.setOnClickListener(this);
        doneButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);



        if (cityId!=null){
            loadData();
        }
        postRecyclerVew = findViewById(R.id.postRecyclerView);

        postRecyclerVew.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        postRecyclerVew.setItemAnimator(new DefaultItemAnimator());

        PostLoading();

    }

    private void loadData() {

        DocumentReference documentReference = firestore.collection("Cities").document(cityId);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error!=null){
                    Log.e("error in loading",error.getMessage());
                }
                else
                {
                    Log.d("123id",value.getId());
                   city = value.getString("City Name");
                    state = value.getString("State Name");
                    country = value.getString("Country Name");
                    visitors = value.getLong("Visitors").intValue();
                    like =  value.getLong("Likes").intValue();
                    imageUrl = value.getString("City Background Image");



                    mainCity.setText(city);
                    mainCountry.setText(country);
                    mainState.setText(state+",");
                    mainVisitors.setText(Integer.toString(visitors)+" visitors");
                    mainLikes.setText(Integer.toString(like)+" likes");



                    Glide.with(getApplicationContext()).load(imageUrl).into(backgroundImage);

                    Log.d("info",city+state+country+imageUrl);
                }

            }
        });
    }

    @Override
    public void onClick(final View view) {
        switch (view.getId()){
            case R.id.changebackground: {
                layout.setVisibility(View.GONE);
               disableScroll();
                fetchWallpaper();
                wallpaperRecyclerView.setVisibility(View.VISIBLE);
                changebackground.setVisibility(View.GONE);
                likebutton.setVisibility(View.GONE);
                confirmchangelayout.setVisibility(View.VISIBLE);
            }
            break;

            case R.id.cancelbutton:{
                layout.setVisibility(View.VISIBLE);
                enableScroll();
                wallpaperRecyclerView.setVisibility(View.GONE);
                changebackground.setVisibility(View.VISIBLE);
                likebutton.setVisibility(View.VISIBLE);
                confirmchangelayout.setVisibility(View.GONE);

            }
            break;

            case R.id.donebutton:

                layout.setVisibility(View.VISIBLE);
                enableScroll();
                if (imageUrl!=recyclerViewChangeWallpaper.selectedImageUrl){
                    DocumentReference documentReference = firestore.collection("Cities").document(cityId);
                    documentReference.update("City Background Image",recyclerViewChangeWallpaper.selectedImageUrl)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Snackbar.make(view,"Background has been changed", BaseTransientBottomBar.LENGTH_LONG);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Snackbar.make(view,"Please try again later.", BaseTransientBottomBar.LENGTH_LONG);
                        }
                    });


                }

                wallpaperRecyclerView.setVisibility(View.GONE);
                changebackground.setVisibility(View.VISIBLE);
                likebutton.setVisibility(View.VISIBLE);
                confirmchangelayout.setVisibility(View.GONE);


        }
    }

    private void disableScroll() {
        final AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams)
                collapsingToolBar.getLayoutParams();
        params.setScrollFlags(0);
        collapsingToolBar.setLayoutParams(params);
    }

    private void enableScroll() {
        final AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams)
               collapsingToolBar.getLayoutParams();
        params.setScrollFlags(
                AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL
                        | AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED
        );
        collapsingToolBar.setLayoutParams(params);
    }

    public void  fetchWallpaper(){



        String url = "https://api.unsplash.com/search/photos?query="+mainCity.getText().toString()+"&client_id=oxQo4AhPw8db0J1kqs9urtDCYtgGm4Kpil2aA8pNyU8&per_page=15";


        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    Log.d("response", response.toString());

                    JSONArray array = response.getJSONArray("results");

                    for (int i =0;i<array.length();i++){
                        JSONObject object = array.getJSONObject(i);
                        String id = object.getString("id");
                        Log.d("photo id", id);

                        JSONObject objectUrls = object.getJSONObject("urls");
                        String rawImageUrl = objectUrls.getString("raw");

                        Log.d("Image url", rawImageUrl);
                        wallpaperModelList.add(new WallpaperModel(id,rawImageUrl));

                    }


                    recyclerViewChangeWallpaper.notifyDataSetChanged();


                }catch (JSONException e){
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext()," Error!!"+e.toString(),Toast.LENGTH_LONG).show();
                    Log.d("error",e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.d("error12",error.toString());
            }
        });

        RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();
        queue.add(objectRequest);
//        VolleySingleton.getInstance().addToRequestQueue(objectRequest);;
        wallpaperModelList.clear();


    }

    public void PostLoading(){

        activityModelList = new ArrayList<>();

        CollectionReference collectionReference = firestore.collection("Posts");
        Query locationquery = collectionReference.whereEqualTo("location",cityId);
        locationquery.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error!=null){
                    error.printStackTrace();
                }
                else{
                    for (QueryDocumentSnapshot snapshot: value){
                        firestore.collection("Posts").document(snapshot.getId())
                                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                                if (error!=null){
                                    error.printStackTrace();
                                }
                                else{
                                    activityModelList.add(new ActivityModel(value.getString("createdBy"),
                                            value.getString("title"),
                                            value.getLong("timeStamp").longValue(),
                                            value.getString("location"),
                                            value.getLong("likes").intValue(),
                                            value.getLong("dislikes").intValue(),
                                            value.getId(),value.getLong("total comments").intValue()));
                                    postRecyclerAdapter.notifyDataSetChanged();

                                    Log.d("inflist",value.getString("createdBy")+value.getString("title"));

                                }
                            }
                        });
                    }
                }
            }
        });

        postRecyclerAdapter = new PostRecyclerAdapter(this,activityModelList);
        postRecyclerVew.setAdapter(postRecyclerAdapter);


    }

}
