package com.example.bucketlist.fragments.homePageFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bucketlist.AddNewCity;
import com.example.bucketlist.R;

public class CityFragment extends Fragment {
    TextView cityText;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_city,container,false);
        cityText = view.findViewById(R.id.cityText);
        cityText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), AddNewCity.class);
                startActivity(i);
            }
        });
        return view;
    }
}
