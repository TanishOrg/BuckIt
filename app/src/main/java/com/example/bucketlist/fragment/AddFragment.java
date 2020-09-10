package com.example.bucketlist.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bucketlist.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

import static android.content.ContentValues.TAG;

public class AddFragment extends Fragment  implements View.OnClickListener {

    MaterialCardView travelCardVew;
    MaterialCardView careerCardView;
    MaterialCardView adventureCardView;
    MaterialCardView relationCardView ;
    MaterialCardView otherCardView ;
    MaterialCardView financialCardView;
    MaterialCardView learningCardView;
    MaterialCardView healthCardView;

    View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_add,container,false);
        travelCardVew = view.findViewById(R.id.travelCardView);
        careerCardView = view.findViewById(R.id.careerCardView);

        initalizeCards();

        return view;
    }

    private void initalizeCards() {
        healthCardView = view.findViewById(R.id.healthCardView);
        careerCardView = view.findViewById(R.id.careerCardView);
        travelCardVew = view.findViewById(R.id.travelCardView);
        adventureCardView  = view.findViewById(R.id.adventureCardView);
        financialCardView = view.findViewById(R.id.financialCardView);
        learningCardView = view.findViewById(R.id.learningCardView);
        otherCardView = view.findViewById(R.id.otherCardView);
        relationCardView = view.findViewById(R.id.relationCardView);


        healthCardView .setOnClickListener(this);
        careerCardView.setOnClickListener(this); ;
        travelCardVew.setOnClickListener(this);
        adventureCardView.setOnClickListener(this);
        financialCardView.setOnClickListener(this);
        learningCardView.setOnClickListener(this);
        otherCardView.setOnClickListener(this); ;
        relationCardView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Log.d(TAG, "onClick: " + "item clicked");
    }
}
