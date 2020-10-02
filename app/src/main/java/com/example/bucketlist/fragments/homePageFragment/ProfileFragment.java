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
import com.example.bucketlist.layout.userLayout.DetailProfile;
import com.example.bucketlist.R;
import com.example.bucketlist.adapters.MyPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.vansuita.gaussianblur.GaussianBlur;


public class ProfileFragment extends Fragment {

    TabLayout tabLayout;
    ViewPager viewPager;
//    AchievedFragment achievedFragment;
//    DreamFragment dreamFragment;
    ImageView profilePageImage,profileBackground;
    TextView profileName;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    String user_id;
    String stringImageUri;
    Context context;



    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_profile,container,false);

        firebaseAuth = FirebaseAuth.getInstance();
        context = getActivity().getApplicationContext();

        tabLayout =(TabLayout) view.findViewById(R.id.tab_bar);
        viewPager = view.findViewById(R.id.viewPager);
        profilePageImage = view.findViewById(R.id.profilePageImage);
        profileName = view.findViewById(R.id.profileName);
        profileBackground = view.findViewById(R.id.profileBackground);


        profileBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplication(), DetailProfile.class);
                startActivity(intent);
            }
        });


//        achievedFragment = new AchievedFragment();
//        dreamFragment = new DreamFragment();

        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getChildFragmentManager());
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(myPagerAdapter);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        user_id = firebaseAuth.getCurrentUser().getUid();
        firebaseFirestore = FirebaseFirestore.getInstance();



        loadData(user_id);

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
                                    GaussianBlur.with(context).radius(6).put(resource,profileBackground);
                                }

                                @Override
                                public void onLoadCleared(@Nullable Drawable placeholder) {

                                }
                            });

                }
            }
        });
    }

}
