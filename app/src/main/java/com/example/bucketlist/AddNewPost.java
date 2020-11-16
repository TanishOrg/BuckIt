package com.example.bucketlist;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bucketlist.model.ActivityModel;
import com.example.bucketlist.model.BucketItems;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddNewPost extends AppCompatActivity implements View.OnClickListener {

    FirebaseAuth mAuth;
    FirebaseUser user;
    ImageView backButton;
    TextView postButton;
    EditText titleText, descriptionText;
    AutoCompleteTextView locationText;
    ChipGroup chipGroup;
    String from;
    String location;
    List<String> availableLocation = new ArrayList<>();
    private InterstitialAd mInterstitialAd;

    private ArrayAdapter<String> arrayAdapter;

    FirebaseFirestore firestore;
    boolean empty = false;
    private String TAG = "AddNewPost";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_post);
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        initialize();

        from = getIntent().getStringExtra("which activity");
        location = getIntent().getStringExtra("location");
        if (from!=null && location!=null && from.equals("cityinnerpage")){
            locationText.setText(location);
        }

    }

    private void initialize(){

        mAuth = FirebaseAuth.getInstance();
//        user = mAuth.getCurrentUser();
        firestore = FirebaseFirestore.getInstance();

        backButton = findViewById(R.id.backButton);
        postButton = findViewById(R.id.postButton);
        titleText = findViewById(R.id.titleText);
        descriptionText = findViewById(R.id.descriptionText);
        locationText = findViewById(R.id.location);
        chipGroup = findViewById(R.id.chipGroup);

        backButton.setOnClickListener(this);
        postButton.setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser() != null) {
            user = mAuth.getCurrentUser();
        }
        getAvailableCity();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.backButton){
            //Toast.makeText(getApplicationContext(), from, Toast.LENGTH_SHORT).show();


            if (location!=null){
             finish();
            }
            else{
                Intent i = new Intent(getApplicationContext(),HomeActivity.class);
                finish();
                i.putExtra("which Activity","from Add new city");
                startActivity(i);
            }


        }
        else if (view.getId() == R.id.postButton){
            if (checkEmptyField(empty)){
                Snackbar.make(view,"Please fill all the fields",Snackbar.LENGTH_LONG).show();
            } else if(!availableLocation.contains(locationText.getText().toString())){
                Snackbar.make(view,"No record for this city found ",Snackbar.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(getApplicationContext(), "Post shared", Toast.LENGTH_SHORT).show();
                /**
                 * Post activity to firebase
                 * Need to post in two location
                 */
                postToFirebase(view);
                Intent i = new Intent(getApplicationContext(),HomeActivity.class);
                finish();
                i.putExtra("which Activity","from Add new city");
                startActivity(i);
            }
        }
    }

    private void postToFirebase(final View v) {
        List<Integer> ids = chipGroup.getCheckedChipIds();
        List<String> categories = new ArrayList<>();
        for (Integer id:ids){
            Chip chip = chipGroup.findViewById(id);
//            Log.d(TAG, "postToFirebase: " + chip.getText().toString().trim());
            categories.add(chip.getText().toString().trim());
        }

        String[] arr = locationText.getText().toString().split(", ",0);
        String cityFilename = arr[0] + ", " + arr[arr.length - 1];
        Map map = new HashMap();
        map.put("title",titleText.getText().toString());
        map.put("description",descriptionText.getText().toString());
        map.put("likes",0);
        map.put("dislikes",0);
        map.put("timeStamp",System.currentTimeMillis());
        map.put("createdBy",user.getUid());
        map.put("location",locationText.getText().toString());
        map.put("category",categories);
        map.put("total comments",0);

//        Log.d(TAG, "postToFirebase: " + chipGroup.getCheckedChipIds().toString());

        DocumentReference activityDocumentReference = firestore.collection("Posts").document();
        activityDocumentReference.set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
               Snackbar.make(v,"Post created", BaseTransientBottomBar.LENGTH_SHORT);
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });

    }

    private boolean checkEmptyField(boolean empty ){
        if (titleText.getText().toString().isEmpty()||
                descriptionText.getText().toString().isEmpty()||
                locationText.getText().toString().isEmpty()){

            empty = true;

        }
        return empty;
    }

    private void getAvailableCity() {
//        availableLocation.clear();
        final CollectionReference reference = firestore.collection("Cities");
        reference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.d("Exception Failed", "onEvent: 0  " + error);
                }

                else{
                    for (QueryDocumentSnapshot snapshot  :value) {
//                        BucketItems item = BucketItems.hashToObject( snapshot.getData(),snapshot.getId());
                        availableLocation.add(snapshot.getId());
                    }
                    Log.d(TAG, "onEvent: " + availableLocation.size());
                    arrayAdapter = new ArrayAdapter<>(getApplicationContext(),
                            android.R.layout.simple_list_item_1,availableLocation);
                    locationText.setAdapter(arrayAdapter);
                    arrayAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        availableLocation.clear();
    }
}