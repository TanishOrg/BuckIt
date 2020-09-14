package com.example.bucketlist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class ProfileFragment extends Fragment {

    TabLayout tabLayout;
    ViewPager viewPager;
    AchievedFragment achievedFragment;
    DreamFragment dreamFragment;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_profile,container,false);

        tabLayout =(TabLayout) view.findViewById(R.id.tab_bar);
        viewPager = view.findViewById(R.id.viewPager);

        achievedFragment = new AchievedFragment();
        dreamFragment = new DreamFragment();

        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getChildFragmentManager());
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(myPagerAdapter);

        return view;
    }

    class MyPagerAdapter extends FragmentPagerAdapter {

        String[] tabName = {"DREAMS", "ACHIEVED"};

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
