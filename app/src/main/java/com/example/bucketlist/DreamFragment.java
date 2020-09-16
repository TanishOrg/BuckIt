package com.example.bucketlist;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bucketlist.data.DatabaseHandler;

public class DreamFragment extends Fragment {
    @Nullable

    RecyclerView recyclerView;
    RecyclerAdapterDream recyclerAdapterDream;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dream,container,false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerAdapterDream =new RecyclerAdapterDream();
        recyclerView.setAdapter(recyclerAdapterDream);


        return view;
    }
}
