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

import com.example.bucketlist.R;
import com.example.bucketlist.adapters.RecyclerAdapterDream;
import com.example.bucketlist.adapters.SwipeToDeleteCallBack;
import com.example.bucketlist.constants.Constants;
import com.example.bucketlist.fragments.homePageFragment.ProfileFragment;
import com.example.bucketlist.model.BucketItemModify;
import com.example.bucketlist.model.BucketItems;
import com.example.bucketlist.model.OnItemDeleteFireBase;
import com.example.bucketlist.model.ProfileFragmentPart;
import com.google.android.gms.tasks.OnCompleteListener;
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

public class DreamFragment extends Fragment
implements ProfileFragmentPart {
    public boolean isActionMode = false;
    @Nullable

    private RecyclerView recyclerView;
    private RecyclerAdapterDream recyclerAdapterDream;
    private List<BucketItems> bucketItems = new ArrayList<>();
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


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.fragment_dream,container,false);
        deleteMultiItem = view.findViewById(R.id.delete_dream);
        floatingOptionLayout = view.findViewById(R.id.floatingOptionLayout);
        counterTextView = view.findViewById(R.id.counterTextView);
        clearButton = view.findViewById(R.id.clearButton);
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
        },deleteMultiItem,clearButton,DreamFragment.this);

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

        Query query = Constants.filterCategory.equals("") ? collection
                .orderBy("dateItemAdded", Query.Direction.DESCENDING)
                .whereEqualTo("achieved", false)
                : collection
                .orderBy("dateItemAdded", Query.Direction.DESCENDING)
                .whereEqualTo("achieved", false)
                .whereEqualTo("category", Constants.filterCategory);;

//               query.addSnapshotListener(new EventListener<QuerySnapshot>() {
//                    @Override
//                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                        if (error != null) {
//                            Log.d("Exception Failed", "onEvent: 0  " + error);
//                        }
//
//                        bucketItems.clear();
//                        for (QueryDocumentSnapshot snapshot  :value) {
//                            BucketItems item = BucketItems.hashToObject( snapshot.getData(),snapshot.getId());
//                            bucketItems.add(item);
//                        }
//                        recyclerAdapterDream.notifyDataSetChanged();
//                    }
//               });

               query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                   @Override
                   public void onComplete(@NonNull Task<QuerySnapshot> task) {
                       if (task.isSuccessful()){
                           bucketItems.clear();
                           for (QueryDocumentSnapshot document : task.getResult()) {
                               BucketItems item = BucketItems.hashToObject( document.getData(),document.getId());
                               bucketItems.add(item);
                           }
                           recyclerAdapterDream.notifyDataSetChanged();
                       }
                   }
               });
    }


    public void startSelection(int position) {
        if (!isActionMode){
            isActionMode = true;
            floatingOptionLayout.setVisibility(View.VISIBLE);
            bucketItemsListToRemove.add(bucketItems.get(position));
            count++;
            updateOptionLayout(count);
            fposition = position;
            recyclerAdapterDream.notifyDataSetChanged();
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
