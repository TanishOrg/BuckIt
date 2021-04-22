package com.example.bucketlist.fragments.profileFragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bucketlist.FirebaseManager;
import com.example.bucketlist.R;
import com.example.bucketlist.adapters.RecyclerAdapterAchieved;
import com.example.bucketlist.adapters.SwipeToDeleteCallBack;
import com.example.bucketlist.constants.Constants;
import com.example.bucketlist.fragments.homePageFragment.ProfileFragment;
import com.example.bucketlist.model.BucketItemModify;
import com.example.bucketlist.model.BucketItems;
import com.example.bucketlist.model.OnItemDeleteFireBase;

import com.example.bucketlist.model.ProfileFragmentPart;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AchievedFragment extends Fragment
implements ProfileFragmentPart  {
    public boolean isActionMode = false;
    RecyclerView recyclerView;
    RecyclerAdapterAchieved recyclerAdapterAchieved;
    List<BucketItems> bucketItems = new ArrayList<>();
    FloatingActionButton fab;
    //    List<BucketItems> items;
    private View view;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseFirestore fireStore;
    private ItemTouchHelper itemTouchHelper;
    private TextView deleteMultiItem;
    public RelativeLayout floatingOptionLayout;
    public TextView counterTextView;
    private ImageView clearButton;
    public List<BucketItems> bucketItemsListToRemove = new ArrayList<>();
    public int count=0;
    public   int fposition = -1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_achieved,container,false);

        deleteMultiItem = view.findViewById(R.id.delete_dream);
        floatingOptionLayout = view.findViewById(R.id.floatingOptionLayout);
        counterTextView = view.findViewById(R.id.counterTextView);
        clearButton = view.findViewById(R.id.clearButton);

        initializeUi();

        return view;
    }

    @Override
    public void initializeUi() {
        mAuth = FirebaseAuth.getInstance();
        recyclerView = view.findViewById(R.id.recyclerViewAchieved);

        recyclerAdapterAchieved =new RecyclerAdapterAchieved(getContext(),bucketItems, new BucketItemModify() {
            @Override
            public void onItemDeleted() {
                refreshFragment();

            }
        }, deleteMultiItem,clearButton,AchievedFragment.this);
        recyclerView.setAdapter(recyclerAdapterAchieved);

        // attaching
        itemTouchHelper = new
                ItemTouchHelper(new SwipeToDeleteCallBack(recyclerAdapterAchieved, new OnItemDeleteFireBase() {
            @Override
            public void onItemDelete() {

            }
        }));
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }

    @Override
    public void onStart() {
        super.onStart();
        mUser = mAuth.getCurrentUser();
        fireStore = FirebaseFirestore.getInstance();

        getDataFromFireStore(fireStore);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (recyclerAdapterAchieved != null)recyclerAdapterAchieved.notifyDataSetChanged();
    }


    @Override
    public void onPause() {
        super.onPause();
        bucketItems.clear();
        recyclerAdapterAchieved.notifyDataSetChanged();
    }

    @Override
    public void refreshFragment() {
        getParentFragment().getParentFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container,new ProfileFragment()).commit();

    }

    @Override
    public void getDataFromFireStore(FirebaseFirestore fireStore) {
//        FirebaseManager manager = FirebaseManager.getManager(getActivity());
//        manager.getAchievedDataFromFireStore(fireStore,bucketItems,recyclerAdapterAchieved);
        CollectionReference collection = fireStore.collection("Users").document(mUser.getUid())
                .collection("items");
////        collection.orderBy("dateItemAdded", Query.Direction.DESCENDING).whereEqualTo("achieved", true).get()
////                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
////            @Override
////            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
////
////                for(QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
////
////                    BucketItems item = BucketItems.hashToObject( snapshot.getData(),snapshot.getId());
////                    bucketItems.add(item);
////                    recyclerAdapterAchieved.notifyDataSetChanged();
////                }
////            }
////        });
//
        Query query = Constants.filterCategory.equals("") ? collection
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

                else{
                    bucketItems.clear();
                    for (QueryDocumentSnapshot snapshot  :value) {
                        BucketItems item = BucketItems.hashToObject( snapshot.getData(),snapshot.getId());
                        bucketItems.add(item);
                    }
                    recyclerAdapterAchieved.notifyDataSetChanged();
                }
            }
        });

//        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if (task.isSuccessful()){
//                    bucketItems.clear();
//                    for (QueryDocumentSnapshot document : task.getResult()) {
//                        BucketItems item = BucketItems.hashToObject( document.getData(),document.getId());
//                        bucketItems.add(item);
//                    }
//                    recyclerAdapterAchieved.notifyDataSetChanged();
//                }
//            }
//        });

    }


    public void startSelection(int position) {
        if (!isActionMode){
            isActionMode = true;
            floatingOptionLayout.setVisibility(View.VISIBLE);
            bucketItemsListToRemove.add(bucketItems.get(position));
            count++;
            updateOptionLayout(count);
            fposition = position;
            recyclerAdapterAchieved.notifyDataSetChanged();
        }
    }

    public void check(View v, int position) {
        if (((CheckBox)v).isChecked()){
            bucketItemsListToRemove.add(bucketItems.get(position));
            count++;
            updateOptionLayout(count);
        }
        else {
            bucketItemsListToRemove.remove(bucketItems.get(position));
            count--;
            updateOptionLayout(count);
        }
    }

    public void updateOptionLayout(int count) {
        if (count ==0){
            counterTextView.setText("0 item selected");
        }
        if (count == 1){
            counterTextView.setText("1 item selected");
        }

        else {
            counterTextView.setText(count+" items selected");
        }
    }
}


