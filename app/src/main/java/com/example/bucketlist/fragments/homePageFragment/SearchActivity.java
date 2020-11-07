package com.example.bucketlist.fragments.homePageFragment;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;

import com.example.bucketlist.R;
import com.example.bucketlist.adapters.PostRecyclerAdapter;
import com.example.bucketlist.model.ActivityModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    PostRecyclerAdapter postRecyclerAdapter;
    List<ActivityModel> list;
    List<ActivityModel> tempList;
    FirebaseFirestore firestore;
    FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        firestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        Toolbar myToolbar = findViewById(R.id.myTool);
        setSupportActionBar(myToolbar);

        recyclerView = findViewById(R.id.recyclerSearchView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        getSupportActionBar().setTitle("Search Here");


        loadData();
//        getActionBar().show();
    }

    private void loadData() {
        list = new ArrayList<>();

        CollectionReference collectionReference = firestore.collection("Posts");
        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                list.clear();
                for (final QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                    list.add(new ActivityModel(snapshot.getString("createdBy"),
                            snapshot.getString("title"),
                            snapshot.getLong("timeStamp").longValue(),
                            snapshot.getString("location"),
                            snapshot.getLong("likes").intValue(),
                            snapshot.getLong("dislikes").intValue(),
                            snapshot.getId(),
                            snapshot.getLong("total comments").intValue()));
                    postRecyclerAdapter.notifyDataSetChanged();

                }
            }
        });

        postRecyclerAdapter = new PostRecyclerAdapter(this,list,"search activity");

        recyclerView.setAdapter(postRecyclerAdapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);

        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setIconified(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                postRecyclerAdapter.getFilter().filter(newText);
                Log.d("TAG", "onQueryTextChange: ");
                return false;
            }
        });
        // Associate searchable configuration with the SearchView
//        SearchManager searchManager =
//                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        SearchView searchView =
//                (SearchView) menu.findItem(R.id.search).getActionView();
//        searchView.setSearchableInfo(
//                searchManager.getSearchableInfo(getComponentName()));

        return true;
    }
}