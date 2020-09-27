package com.example.bucketlist.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.bucketlist.fragments.profileFragments.AchievedFragment;
import com.example.bucketlist.fragments.profileFragments.DreamFragment;

public class MyPagerAdapter extends FragmentPagerAdapter {

    AchievedFragment achievedFragment;
    DreamFragment dreamFragment;

    String[] tabName = {"ACTIVE", "ACHIEVED"};

    public MyPagerAdapter(@NonNull FragmentManager fm)
    {
        super(fm);
        achievedFragment = new AchievedFragment();
        dreamFragment = new DreamFragment();
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

