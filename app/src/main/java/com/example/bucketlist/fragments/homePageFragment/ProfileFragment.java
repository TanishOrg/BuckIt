package com.example.bucketlist.fragments.homePageFragment;


import android.content.Context;
import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;

import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.bucketlist.constants.Constants;
import com.example.bucketlist.layout.userLayout.DetailProfile;
import com.example.bucketlist.R;
import com.example.bucketlist.adapters.MyPagerAdapter;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.vansuita.gaussianblur.GaussianBlur;

import static android.content.ContentValues.TAG;


public class ProfileFragment extends Fragment {


    TabLayout tabLayout;
    public ViewPager viewPager;
    ImageView profilePageImage,profileBackground;
    TextView profileName;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    String user_id;
    String stringImageUri;
    ChipGroup chipGroup;
    Context context;
    MyPagerAdapter myPagerAdapter;
    private static int lastPosition = 0;
    private static int lastPositionChip = 0;

    private boolean isSelected = false;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_profile,container,false);

        firebaseAuth = FirebaseAuth.getInstance();
        context = getActivity().getApplicationContext();

        tabLayout =(TabLayout) view.findViewById(R.id.tab_bar);
        viewPager = view.findViewById(R.id.viewPager);
        profilePageImage = view.findViewById(R.id.profilePageImage);
        profileName = view.findViewById(R.id.profileName);
        profileBackground = view.findViewById(R.id.profileBackground);
        chipGroup = view.findViewById(R.id.chip_group_profile);

        profileBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplication(), DetailProfile.class);
                startActivity(intent);
            }
        });




        myPagerAdapter = new MyPagerAdapter(getChildFragmentManager());
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(myPagerAdapter);



        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        viewPager.setCurrentItem(lastPosition);
        user_id = firebaseAuth.getCurrentUser().getUid();
        firebaseFirestore = FirebaseFirestore.getInstance();
        chipGroup.setSingleSelection(true);
        loadData(user_id);
        Log.d(TAG, "onStart: " + chipGroup.toString());
//        Log.d(TAG, "onStart: " + chipGroup.getChildAt(1));
        chipGroup.check(lastPositionChip);

        chipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {
                Log.d(TAG, "onCheckedChanged: " +  checkedId + "ID"  );
                lastPositionChip = checkedId;
                changeCategory(group,checkedId);
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container,new ProfileFragment()).commit();
            }
        });
    }

    private void changeCategory(ChipGroup group, int checkedId) {
        if (checkedId != -1) {
            Chip chip = group.findViewById(checkedId);
            Log.d(TAG, "changeCategory: " + chip.getText());
            Constants.filterCategory = chip.getText().toString().trim();
        } else  {
            Constants.filterCategory = "";
        }

    }

    private void loadData(String user_id) {
        DocumentReference documentReference = firebaseFirestore.collection("Users").document(user_id);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error!=null){
                    Log.d("Profile Fragment error","Error:"+error.getMessage());
                }
                else {
                    profileName.setText(value.getString("Display Name"));
                    stringImageUri = value.getString("Image Uri");
                    Glide.with(context).load(stringImageUri).into(profilePageImage);
                    Glide.with(context)
                            .load(stringImageUri)
                            .into(new CustomTarget<Drawable>() {
                                @Override
                                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                    GaussianBlur.with(context).radius(20).put(resource,profileBackground);
                                }

                                @Override
                                public void onLoadCleared(@Nullable Drawable placeholder) {

                                }
                            });

                }
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        lastPosition = viewPager.getCurrentItem();

    }



}
