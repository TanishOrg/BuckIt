package com.example.bucketlist;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bucketlist.adapters.RecyclerAdapterWallpaper;
import com.example.bucketlist.fragments.homePageFragment.CityFragment;
import com.example.bucketlist.fragments.homePageFragment.ProfileFragment;
import com.example.bucketlist.layout.userLayout.DetailProfile;
import com.example.bucketlist.layout.userLayout.UserDetail;
import com.example.bucketlist.model.WallpaperModel;
import com.example.bucketlist.utils.CityListHelper;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.JsonArray;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;

public class AddNewCity extends AppCompatActivity implements View.OnClickListener {

    Button cancelButton , createButton;
    AutoCompleteTextView addCityEditText;
    RecyclerView recyclerView;
    String City;
    RecyclerAdapterWallpaper recyclerAdapterWallpaper;
    List<WallpaperModel> wallpaperModelList;
    RelativeLayout smallRelativeLayout;
    ProgressBar progressBar;
    StorageReference storageReference;
    FirebaseFirestore firestore;
    FirebaseAuth firebaseAuth;
    private InterstitialAd mInterstitialAd;
    List<String> existinglocation = new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;

    private static List<String> city= new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_city);



        cancelButton = findViewById(R.id.cancelButton);
        createButton = findViewById(R.id.createButton);
        addCityEditText = findViewById(R.id.addCityEditText);
        smallRelativeLayout = findViewById(R.id.smallRelativeLayout);
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);
        wallpaperModelList = new ArrayList<>();
        recyclerAdapterWallpaper = new RecyclerAdapterWallpaper(this,wallpaperModelList,AddNewCity.this);
        storageReference = FirebaseStorage.getInstance().getReference().child("city background");
        firestore =FirebaseFirestore.getInstance();

        recyclerView.setAdapter(recyclerAdapterWallpaper);
        cancelButton.setOnClickListener(this);
        smallRelativeLayout.setOnClickListener(this);
        createButton.setOnClickListener(this);
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
    }

    private void loadCityList(String search) {
        CityListHelper cityListHelper = new CityListHelper(this,search) {

            @Override
            protected void onCompleteListener(List<CityQuery> queryResult) {
                Log.d("Listener ", "onCompleteListener: " + queryResult.toString());
                List<String> list = new ArrayList<>();
                for (CityQuery query: queryResult
                ) {
                    list.add(query.getDisplayName());
                }
                city = list;
                arrayAdapter = new ArrayAdapter<>(getApplicationContext(),
                        android.R.layout.simple_list_item_1,city);
                addCityEditText.setAdapter(arrayAdapter);
                arrayAdapter.notifyDataSetChanged();
            }
        };
    }

    public void  fetchWallpaper(){

        String url = "https://api.unsplash.com/search/photos?query="+City+"&client_id=oxQo4AhPw8db0J1kqs9urtDCYtgGm4Kpil2aA8pNyU8&per_page=15";


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
                        WallpaperModel wallpaperModel = new WallpaperModel(id,rawImageUrl);
                        wallpaperModelList.add(wallpaperModel);

                    }
                    recyclerAdapterWallpaper.notifyDataSetChanged();


                }catch (JSONException e){
                    e.printStackTrace();
                    Toast.makeText(AddNewCity.this," Error!!"+e.toString(),Toast.LENGTH_LONG).show();
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

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.cancelButton){
            Intent i = new Intent(AddNewCity.this,HomeActivity.class);
            finish();
            i.putExtra("which Activity","from Add new city");
            startActivity(i);

        }
        if (v.getId() == R.id.smallRelativeLayout){
            getExistingCity();
            if(!addCityEditText.getText().toString().isEmpty()){
                if (!existinglocation.contains(addCityEditText.getText().toString())){
                    City = addCityEditText.getText().toString().split(",")[0];
                    Log.d("searched city",City);
                    fetchWallpaper();
                    recyclerView.setVisibility(View.VISIBLE);
                }
                else{
                    Toast.makeText(this, "City already exists", Toast.LENGTH_SHORT).show();
                }

            }
            else{
                recyclerView.setVisibility(View.GONE);
                Toast.makeText(this, "Field is empty", Toast.LENGTH_SHORT).show();

            }
        }

        else if (v.getId() == R.id.createButton){

            if (!addCityEditText.getText().toString().isEmpty()){
                if (recyclerAdapterWallpaper.selectedImagePosition != -1){
                    progressBar.setVisibility(View.VISIBLE);
                    uploadToFireStore(recyclerAdapterWallpaper.selectedImageUrl);
                    Intent i = new Intent(AddNewCity.this,HomeActivity.class);
                    finish();
                    progressBar.setVisibility(View.GONE);
                    i.putExtra("which Activity","from Add new city");
                    startActivity(i);


                }
                else
                    Snackbar.make(v,"Please select the background",Snackbar.LENGTH_LONG).show();

            }
            else
                Snackbar.make(v,"Enter the City ",Snackbar.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        addCityEditText.addTextChangedListener(tf);
    }

    TextWatcher tf = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            Log.d("Ok", "afterTextChanged: " + s.toString());
            loadCityList(s.toString());
        }
    };



    public void uploadToFireStore(String StringCityImageUri){
        String[] arr = addCityEditText.getText().toString().split(", ",0);
        String cityFilename = arr[0] + ", " + arr[arr.length - 1];
        long timeInMilliSeconds = System.currentTimeMillis();
        DocumentReference documentReference = firestore.collection("Cities").document(addCityEditText.getText().toString());
        Map<String,Object> user = new HashMap<>();
        user.put("City Name",arr[0]);
        user.put("State Name",arr[1]);
        user.put("Country Name",arr[2]);
        user.put("City Background Image",StringCityImageUri);
        user.put("Visitors",0);
        user.put("Likes",0);
        user.put("Time of Creation",timeInMilliSeconds);
        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(AddNewCity.this, "City created", Toast.LENGTH_SHORT).show();
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddNewCity.this, "firestore Updated failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getExistingCity(){
        final CollectionReference reference = firestore.collection("Cities");
        reference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.d("Exception Failed", "onEvent: 0  " + error);
                }

                else{
                    for (QueryDocumentSnapshot snapshot  :value) {
                        existinglocation.add(snapshot.getId());
                    }
                    Log.d("size", "onEvent: " + existinglocation.size());
//                    arrayAdapter = new ArrayAdapter<>(getApplicationContext(),
//                            android.R.layout.simple_list_item_1,existinglocation);
//                    locationText.setAdapter(arrayAdapter);
//                    arrayAdapter.notifyDataSetChanged();
                }
            }
        });
    }
}