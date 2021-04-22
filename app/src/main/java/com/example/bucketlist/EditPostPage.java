package com.example.bucketlist;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditPostPage extends AppCompatActivity implements View.OnClickListener {

    FirebaseAuth mAuth;
    FirebaseUser user;
    ImageView backButton;
    TextView saveButton;
    EditText titleText, descriptionText;
    AutoCompleteTextView locationText;
    ChipGroup chipGroup;
    String stitle,sdes,sloc;
    List<String> availableLocation = new ArrayList<>();
    private ArrayAdapter<String> arrayAdapter;
    List<String> categorylist;
    String postId;

    FirebaseFirestore firestore;
    boolean empty = false;
    private String TAG = "AddNewPost";
    List<Integer> ids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_post_page);
        postId = getIntent().getStringExtra("postid");
        initialize();
    }

    private void initialize(){

        mAuth = FirebaseAuth.getInstance();
//        user = mAuth.getCurrentUser();
        firestore = FirebaseFirestore.getInstance();
        ids = new ArrayList<>();
        backButton = findViewById(R.id.backButton);
        saveButton = findViewById(R.id.saveButton);
        titleText = findViewById(R.id.titleText);
        descriptionText = findViewById(R.id.descriptionText);
        locationText = findViewById(R.id.location);
        chipGroup = findViewById(R.id.chipGroup);
        ids.add(R.id.chip_travel);
        ids.add(R.id.chip_food);
        ids.add(R.id.chip_adventure);
        ids.add(R.id.chip_career);
        ids.add(R.id.chip_relation);
        ids.add(R.id.chip_financial);
        ids.add(R.id.chip_health);
        ids.add(R.id.chip_other);
        ids.add(R.id.chip_learning);


        backButton.setOnClickListener(this);
        saveButton.setOnClickListener(this);

        try {
            if (postId!=null){

                loadingData();
            }
        }catch (Exception e){
            e.printStackTrace();
        }


//        List<Integer> latestids = chipGroup.getCheckedChipIds();
//        List<String> categories = new ArrayList<>();
//        for (Integer id:latestids){
//            Chip chip = chipGroup.findViewById(id);
////            Log.d(TAG, "postToFirebase: " + chip.getText().toString().trim());
//            categories.add(chip.getText().toString().trim());
//        }
//
//        if (categories.equals(categorylist)){
//            saveButton.setVisibility(View.GONE);
//        }
//        else {
//            saveButton.setVisibility(View.VISIBLE);
//        }

        getAvailableCity();

    }

    private void loadingData() {
       DocumentReference documentReference =  firestore.collection("Posts").document(postId);
       documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
           @Override
           public void onComplete(@NonNull Task<DocumentSnapshot> task) {
               if (task.isSuccessful()){
                   DocumentSnapshot documentSnapshot = task.getResult();
                   if (documentSnapshot.exists()){
                       stitle = documentSnapshot.getString("title");
                       sdes = documentSnapshot.getString("description");
                       sloc = documentSnapshot.getString("location");
                       titleText.setText(stitle);
                       descriptionText.setText(sdes);
                       locationText.setText(sloc);
                      categorylist = (List<String>)documentSnapshot.get("category");
                       Log.d("category",categorylist.toString());
                       Log.d("IDSy",ids.toString());

                       for (Integer id : ids){
                           for (String category:categorylist){
                               Chip chip = chipGroup.findViewById(id);
                               if (chip.getText().toString().trim().equals(category)){
                                   chip.setChecked(true);
                               }
                           }


                       }
                   }
               }
           }
       });


    }





    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.backButton){
            finish();
        }
        else if (view.getId()==R.id.saveButton){
            saveChanges();
        }
    }

    private void saveChanges() {
                List<Integer> latestids = chipGroup.getCheckedChipIds();
        List<String> categories = new ArrayList<>();
        for (Integer id:latestids){
            Chip chip = chipGroup.findViewById(id);
//            Log.d(TAG, "postToFirebase: " + chip.getText().toString().trim());
            categories.add(chip.getText().toString().trim());
        }
         if(!availableLocation.contains(locationText.getText().toString())){
             Toast.makeText(this, "No record for this city found ", Toast.LENGTH_SHORT).show();
        }

        else if (!sloc.equals(locationText.getText().toString()) ||
                !sdes.equals(descriptionText.getText().toString()) ||
                !stitle.equals(titleText.getText().toString()) ||
                !categories.equals(categorylist)){
            Map changemap = new HashMap();
            changemap.put("title",titleText.getText().toString());
            changemap.put("description",descriptionText.getText().toString());
            changemap.put("location",locationText.getText().toString());
            changemap.put("category",categories);
            firestore.collection("Posts").document(postId).update(changemap).addOnSuccessListener(new OnSuccessListener() {
                @Override
                public void onSuccess(Object o) {
                    Intent go = new Intent(getApplicationContext(),HomeActivity.class);
                   go.putExtra("which Activity","from Add new city");
                    finish();
                    startActivity(go);
                }
            });

        }
        else{
            Toast.makeText(this, "NO CHANGES ARE MADE", Toast.LENGTH_SHORT).show();
        }


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