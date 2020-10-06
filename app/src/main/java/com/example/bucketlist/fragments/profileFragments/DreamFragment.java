package com.example.bucketlist.fragments.profileFragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bucketlist.R;
import com.example.bucketlist.adapters.RecyclerAdapterDream;
import com.example.bucketlist.adapters.SwipeToDeleteCallBack;
import com.example.bucketlist.fragments.homePageFragment.ProfileFragment;
import com.example.bucketlist.model.BucketItemModify;
import com.example.bucketlist.model.BucketItems;
import com.example.bucketlist.model.OnItemDelete;
import com.example.bucketlist.model.OnItemDeleteFireBase;
import com.example.bucketlist.model.ProfileFragmentPart;
import com.google.android.gms.tasks.OnSuccessListener;
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

public class DreamFragment extends Fragment
implements ProfileFragmentPart {
    @Nullable

    private RecyclerView recyclerView;
    private RecyclerAdapterDream recyclerAdapterDream;
    private List<BucketItems> bucketItems = new ArrayList<>();
    private View view;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseFirestore fireStore;
    private ItemTouchHelper itemTouchHelper;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.fragment_dream,container,false);
         initializeUi();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mUser = mAuth.getCurrentUser();
        fireStore = FirebaseFirestore.getInstance();
        getDataFromFireStore(fireStore);

//        DatabaseHandler db = new DatabaseHandler(getContext());
//        if (db.getItemsCount() >0) items = db.getAllItems();
//        recyclerView = view.findViewById(R.id.recyclerView);
//        recyclerAdapterDream =new RecyclerAdapterDream(getContext(),bucketItems);
//        recyclerView.setAdapter(recyclerAdapterDream);

    }
    @Override
    public void onPause() {
        super.onPause();
        bucketItems.clear();
        recyclerAdapterDream.notifyDataSetChanged();
    }

    @Override
    public void initializeUi() {
        recyclerView = view.findViewById(R.id.recyclerViewDream);

        mAuth = FirebaseAuth.getInstance();

        recyclerAdapterDream =new RecyclerAdapterDream(getContext(), bucketItems, new BucketItemModify() {
            @Override
            public void onItemDeleted() {
                refreshFragment();
            }
        });

        recyclerView.setAdapter(recyclerAdapterDream);

        //itemTouch helper
        itemTouchHelper = new
                ItemTouchHelper(new SwipeToDeleteCallBack(recyclerAdapterDream, new OnItemDeleteFireBase() {
            @Override
            public void onItemDelete() {

            }
        }));
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public void refreshFragment() {
        getParentFragment().getParentFragmentManager()
                .beginTransaction().replace(R.id.fragment_container,new ProfileFragment()).commit();

    }

    @Override
    public void getDataFromFireStore(FirebaseFirestore fireStore) {
        CollectionReference collection = fireStore.collection("Users").document(mUser.getUid())
                .collection("items");
//        collection.orderBy("dateItemAdded", Query.Direction.DESCENDING).whereEqualTo("achieved", false).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//            @Override
//            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//
//                for(QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
//                    BucketItems item = BucketItems.hashToObject( snapshot.getData(),snapshot.getId());
//                    bucketItems.add(item);
//                }
//
//                recyclerAdapterDream.notifyDataSetChanged();
//
//            }
//        });

        collection
                .orderBy("dateItemAdded", Query.Direction.DESCENDING)
                .whereEqualTo("achieved", false)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
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
                        recyclerAdapterDream.notifyDataSetChanged();
                    }
                });
    }


}
