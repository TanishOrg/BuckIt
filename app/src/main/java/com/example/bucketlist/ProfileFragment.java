package com.example.bucketlist;


import android.content.Intent;

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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;


public class ProfileFragment extends Fragment {

    TabLayout tabLayout;
    ViewPager viewPager;
    AchievedFragment achievedFragment;
    DreamFragment dreamFragment;


    ImageView profilePageImage,profileBackground;
    TextView profileName;

    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;

    String user_id;
    String stringImageUri;



    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_profile,container,false);

        tabLayout =(TabLayout) view.findViewById(R.id.tab_bar);
        viewPager = view.findViewById(R.id.viewPager);
        profilePageImage = view.findViewById(R.id.profilePageImage);
        profileName = view.findViewById(R.id.profileName);
        profileBackground = view.findViewById(R.id.profileBackground);
        profileBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplication(),DetailProfile.class);
                startActivity(intent);
            }
        });



        achievedFragment = new AchievedFragment();
        dreamFragment = new DreamFragment();

        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getChildFragmentManager());
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(myPagerAdapter);

        firebaseAuth = FirebaseAuth.getInstance();
        user_id = firebaseAuth.getCurrentUser().getUid();

        firebaseFirestore = FirebaseFirestore.getInstance();

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
                    Glide.with(getContext().getApplicationContext()).load(stringImageUri).into(profilePageImage);
                }


            }
        });



        return view;
    }

    class MyPagerAdapter extends FragmentPagerAdapter {

        String[] tabName = {"ACTIVE", "ACHIEVED"};

        public MyPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {

            switch (position){
                case 0:
                    return dreamFragment;
                case 1:
                    return achievedFragment;
            }
            return null;
        }

        @Override
        public int getCount() {
            return tabName.length;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return tabName[position];
        }
    }


}
