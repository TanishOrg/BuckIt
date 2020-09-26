package com.example.bucketlist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bucketlist.adapters.RecyclerAdapterAchieved;
import com.example.bucketlist.adapters.RecyclerAdapterDream;
import com.example.bucketlist.model.BucketItems;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class DreamFragment extends Fragment {
    @Nullable

    RecyclerView recyclerView;
    RecyclerAdapterDream recyclerAdapterDream;
    List<BucketItems> bucketItems = new ArrayList<>();
//    List<BucketItems> items;
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
        collection.orderBy("dateItemAdded", Query.Direction.DESCENDING).whereEqualTo("achieved", false).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for(QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
//                    Log.d(TAG, "onSuccess: document id " + snapshot.getId());
                    BucketItems item = BucketItems.hashToObject( snapshot.getData(),snapshot.getId());
                    bucketItems.add(item);
                }

                recyclerView = view.findViewById(R.id.recyclerViewDream);
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

    @Override
    public void onResume() {
        super.onResume();
        if (recyclerAdapterDream != null) recyclerAdapterDream.notifyDataSetChanged();
    }
}
