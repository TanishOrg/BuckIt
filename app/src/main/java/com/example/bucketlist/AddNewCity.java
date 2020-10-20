package com.example.bucketlist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
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
import com.example.bucketlist.model.WallpaperModel;
import com.example.bucketlist.utils.CityListHelper;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
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
        wallpaperModelList = new ArrayList<>();
        recyclerAdapterWallpaper = new RecyclerAdapterWallpaper(this,wallpaperModelList);

        recyclerView.setAdapter(recyclerAdapterWallpaper);
        cancelButton.setOnClickListener(this);
        smallRelativeLayout.setOnClickListener(this);

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


        }
        if (v.getId() == R.id.smallRelativeLayout){
            if(!addCityEditText.getText().toString().isEmpty()){
                City = addCityEditText.getText().toString();
                fetchWallpaper();
              recyclerView.setVisibility(View.VISIBLE);
            }
            else{
                recyclerView.setVisibility(View.GONE);
                Snackbar.make(v,"Field is empty.",Snackbar.LENGTH_LONG).show();
            }

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
}