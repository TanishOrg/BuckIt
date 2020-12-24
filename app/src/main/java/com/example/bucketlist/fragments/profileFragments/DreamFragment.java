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
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
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
    private List bucketItems = new ArrayList<>();
    private View view;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseFirestore fireStore;
    private ItemTouchHelper itemTouchHelper;
    private TextView deleteMultiItem;
    public RelativeLayout floatingOptionLayout;
    public TextView counterTextView;
    private ImageView clearButton;
    public List bucketItemsListToRemove = new ArrayList<>();
    public int count=0;
    public   int fposition = -1;

    public static final String BANNER_AD_ID = "ca-app-pub-9294609745811905/6910554364";
    public static final String BANNER_TEST_ID = "ca-app-pub-3940256099942544/6300978111";


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

        MobileAds.initialize(getContext().getApplicationContext());

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
        
        Query query = Constants.filterCategory.equals("") ? collection
                .orderBy("dateItemAdded", Query.Direction.DESCENDING)
                .whereEqualTo("achieved", false)
                : collection
                .orderBy("dateItemAdded", Query.Direction.DESCENDING)
                .whereEqualTo("achieved", false)
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

                            //todo - changes has been made to add ads in the recycler view
                            int size = bucketItems.size();
                            for (int i= size-1; i >=0; i -= 4) {
                                final AdView adView = new AdView(getContext());
                                adView.setAdSize(AdSize.BANNER);
//                                adView.setAdUnitId(BANNER_AD_ID);
                                adView.setAdUnitId(BANNER_TEST_ID);
                                bucketItems.add(i, adView);
                            }

                            for (int i = 0; i < bucketItems.size(); i++) {
                                Object adView = bucketItems.get(i);

                                if (adView instanceof AdView) {
                                    final AdView view = (AdView) adView;

                                    view.loadAd(new AdRequest.Builder().build());
                                }
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
