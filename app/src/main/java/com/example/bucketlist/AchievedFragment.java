package com.example.bucketlist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.bucketlist.adapters.RecyclerAdapterAchieved;
import com.example.bucketlist.adapters.RecyclerAdapterDream;
import com.example.bucketlist.model.BucketItemModify;
import com.example.bucketlist.model.BucketItems;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.util.ArrayList;
import java.util.List;

public class AchievedFragment extends Fragment {

    RecyclerView recyclerView;
    RecyclerAdapterAchieved recyclerAdapterAchieved;
    List<BucketItems> bucketItems = new ArrayList<>();
    //    List<BucketItems> items;
    private View view;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseFirestore fireStore;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_achieved,container,false);
        mAuth = FirebaseAuth.getInstance();

        recyclerView = view.findViewById(R.id.recyclerViewAchieved);
        recyclerAdapterAchieved =new RecyclerAdapterAchieved(getContext(),bucketItems, new BucketItemModify() {
            @Override
            public void onItemDeleted() {
                refreshFragment();
            }
        });
        recyclerView.setAdapter(recyclerAdapterAchieved);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mUser = mAuth.getCurrentUser();
        fireStore = FirebaseFirestore.getInstance();
        CollectionReference collection = fireStore.collection("Users").document(mUser.getUid())
                .collection("items");
        collection.orderBy("dateItemAdded", Query.Direction.DESCENDING).whereEqualTo("achieved", true).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for(QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
//                    Log.d(TAG, "onSuccess: document id " + snapshot.getId());
                    BucketItems item = BucketItems.hashToObject( snapshot.getData(),snapshot.getId());
                    bucketItems.add(item);
                    recyclerAdapterAchieved.notifyDataSetChanged();
                }

            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        if (recyclerAdapterAchieved != null)recyclerAdapterAchieved.notifyDataSetChanged();
    }


    private void refreshFragment() {
        getParentFragment().getParentFragmentManager()
                .beginTransaction().replace(R.id.fragment_container,new ProfileFragment()).commit();
        final ViewPager viewPager = getParentFragment().getView().findViewById(R.id.viewPager);
        viewPager.setCurrentItem(1);
//        tabLayout.getTabAt(1).select();
    }

    @Override
    public void onPause() {
        super.onPause();
        bucketItems.clear();
        recyclerAdapterAchieved.notifyDataSetChanged();
    }
}


