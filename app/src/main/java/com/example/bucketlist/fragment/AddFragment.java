package com.example.bucketlist.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bucketlist.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AddFragment extends Fragment {
    @Nullable
    @Override


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        BottomNavigationView navBar = getActivity().findViewById(R.id.bottom_nav);
        return inflater.inflate(R.layout.fragment_add,container,false);

    }
}
