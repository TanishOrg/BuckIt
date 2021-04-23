package com.example.bucketlist;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.bucketlist.adapters.RecyclerAdapterAchieved;
import com.example.bucketlist.constants.Constants;
import com.example.bucketlist.model.BucketItems;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class FirebaseManager {
    public static FirebaseManager manager;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseFirestore firebaseFirestore;
    private Activity activity;

    public FirebaseManager(Activity activity) {
        this.activity = activity;
    }




    public static FirebaseManager getManager(Activity activity) {
        if (manager == null) {
            manager = new FirebaseManager(activity);
            manager.mAuth = FirebaseAuth.getInstance();
            if ( manager.mAuth.getCurrentUser() != null) manager.mUser = manager.mAuth.getCurrentUser();
            manager.firebaseFirestore = FirebaseFirestore.getInstance();
        }
        return manager;
    }

    public void getAchievedDataFromFireStore(FirebaseFirestore fireStore, final List<BucketItems> bucketItems, final RecyclerAdapterAchieved recyclerAdapterAchieved) {
        CollectionReference collection = fireStore.collection("Users").document(mUser.getUid())
                .collection("items");
//        collection.orderBy("dateItemAdded", Query.Direction.DESCENDING).whereEqualTo("achieved", true).get()
//                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//            @Override
//            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//
//                for(QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
//
//                    BucketItems item = BucketItems.hashToObject( snapshot.getData(),snapshot.getId());
//                    bucketItems.add(item);
//                    recyclerAdapterAchieved.notifyDataSetChanged();
//                }
//            }
//        });

        Query query = com.example.bucketlist.constants.Constants.filterCategory.equals("") ? collection
                .orderBy("dateItemAdded", Query.Direction.DESCENDING)
                .whereEqualTo("achieved", true)
                : collection
                .orderBy("dateItemAdded", Query.Direction.DESCENDING)
                .whereEqualTo("achieved", true)
                .whereEqualTo("category", Constants.filterCategory);;

        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.d("Exception Failed", "onEvent: 0  " + error);
                }


                bucketItems.clear();
                for (QueryDocumentSnapshot snapshot  :value) {
                    BucketItems item = BucketItems.hashToObject( snapshot.getData(),snapshot.getId());
                    bucketItems.add(item);
                }
                recyclerAdapterAchieved.notifyDataSetChanged();
            }
        });

    }


}
