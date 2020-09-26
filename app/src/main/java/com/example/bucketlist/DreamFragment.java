package com.example.bucketlist;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bucketlist.data.DatabaseHandler;
import com.example.bucketlist.model.BucketItems;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class DreamFragment extends Fragment {
    @Nullable

    RecyclerView recyclerView;
    RecyclerAdapterDream recyclerAdapterDream;
    List<BucketItems> bucketItems = new ArrayList<>();
    List<BucketItems> items;
    private View view;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseFirestore fireStore;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.fragment_dream,container,false);

        mAuth = FirebaseAuth.getInstance();


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mUser = mAuth.getCurrentUser();
        fireStore = FirebaseFirestore.getInstance();
        CollectionReference collection = fireStore.collection("Users").document(mUser.getUid())
                .collection("items");
        collection.orderBy("dateItemAdded", Query.Direction.DESCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for(QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                    BucketItems item = BucketItems.hashToObject( snapshot.getData());
                    bucketItems.add(item);
                }
                for (BucketItems item : bucketItems) {
                    Log.d(TAG, "onSuccess Items: " + item.toString());
                }
                recyclerView = view.findViewById(R.id.recyclerView);
                recyclerAdapterDream =new RecyclerAdapterDream(getContext(),bucketItems);
                recyclerView.setAdapter(recyclerAdapterDream);
            }
        });

//        DatabaseHandler db = new DatabaseHandler(getContext());
//        if (db.getItemsCount() >0) items = db.getAllItems();
//        recyclerView = view.findViewById(R.id.recyclerView);
//        recyclerAdapterDream =new RecyclerAdapterDream(getContext(),bucketItems);
//        recyclerView.setAdapter(recyclerAdapterDream);

    }
}
