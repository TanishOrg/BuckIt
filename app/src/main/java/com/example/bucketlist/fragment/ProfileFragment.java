package com.example.bucketlist.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.example.bucketlist.R;
import com.example.bucketlist.layout.AchievedFragment;
import com.example.bucketlist.layout.DreamsFragment;
import com.example.bucketlist.layout.pagerAdapter;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class ProfileFragment extends Fragment {

    private static final String TAG = "PROFILE FRAGMENT";
    private TabLayout tabLayout;
    private TabItem tabDreams;
    private  TabItem tabAchieved;
    private ViewPager viewPager;
    private pagerAdapter pagerAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile,container,false);

        tabLayout =(TabLayout) view.findViewById(R.id.tab_bar);
        tabDreams = view.findViewById(R.id.tabDreams);
        tabAchieved = view.findViewById(R.id.tabAchieved);
        final ViewPager viewPager = view.findViewById(R.id.viewPager);
        pagerAdapter = new pagerAdapter(getActivity(),getChildFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);

        
        viewPager.setCurrentItem(0);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        return view;


    }

}
