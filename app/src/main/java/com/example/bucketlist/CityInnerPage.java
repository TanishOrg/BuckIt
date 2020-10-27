package com.example.bucketlist;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class CityInnerPage extends AppCompatActivity implements View.OnClickListener {
    TextView cityName,stateName,countryname,visitorNo,likeNo;
    String cityId,imageUrl,city,state,country;
    int like,visitors;
    ImageView backgroundImage;
    FirebaseFirestore firestore;
    FloatingActionButton fabCreatePost;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.city_inner_page);
        cityId = getIntent().getStringExtra("cityId");
        firestore = FirebaseFirestore.getInstance();
        cityName = findViewById(R.id.cityName);
        stateName = findViewById(R.id.stateName);
        countryname = findViewById(R.id.countryName);
        visitorNo = findViewById(R.id.visitorno);
        likeNo = findViewById(R.id.likeNo);
        backgroundImage = findViewById(R.id.backgroundImage);
        fabCreatePost = findViewById(R.id.fabCreatePost);

        fabCreatePost.setOnClickListener(this);


        loadData();

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

                    cityName.setText(city);
                    countryname.setText(country);
                    stateName.setText(state+",");
                    visitorNo.setText(Integer.toString(visitors)+" visitors");
                    likeNo.setText(Integer.toString(like)+" likes");

                    Glide.with(getApplicationContext()).load(imageUrl).into(backgroundImage);

                    Log.d("info",city+state+country+imageUrl);
                }

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fabCreatePost:
            {
                Intent i = new Intent(getApplicationContext(),AddNewPost.class);
                i.putExtra("which activity","cityinneractivity");
                i.putExtra("location",cityId);
                startActivity(i);
            }
        }
    }
}
