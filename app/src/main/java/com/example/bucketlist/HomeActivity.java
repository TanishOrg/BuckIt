package com.example.bucketlist;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.bucketlist.fragments.homePageFragment.AddFragment;
import com.example.bucketlist.fragments.homePageFragment.CityFragment;
import com.example.bucketlist.fragments.homePageFragment.ProfileFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = HomeActivity.class.getSimpleName() ;

    FirebaseAuth  firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    String recentPassword , storedPassword ,from;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

      //  getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        try {
            from = getIntent().getStringExtra("from activity");

            if (from.equals("LoginActivity")){
                firebaseAuth = FirebaseAuth.getInstance();
                firebaseFirestore = FirebaseFirestore.getInstance();
                final DocumentReference documentReference = firebaseFirestore.collection("Users").document(firebaseAuth.getCurrentUser().getUid());
                recentPassword = getIntent().getStringExtra("password");
                documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (value!=null)
                            storedPassword = value.getString("User password");
                    }
                });
                if (!recentPassword.equals(storedPassword)){
                    documentReference.update("User password",recentPassword);
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }


        final ChipNavigationBar bottomNav = findViewById(R.id.bottom_nav);
       FragmentManager fragmentManager = getSupportFragmentManager();


        if(savedInstanceState == null) {
            getSupportFragmentManager().
                    beginTransaction().replace(R.id.fragment_container,new ProfileFragment()).commit();
            bottomNav.setItemSelected(R.id.profile,true);
        }

        bottomNav.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int id) {
                Fragment selectedFragment = null;
                switch (id){
                    case R.id.add:
                        selectedFragment = new AddFragment();
                        break;
                    case R.id.city:
                        selectedFragment = new CityFragment();
                        break;
                    case R.id.profile:
                        selectedFragment = new ProfileFragment();
                        break;

                }
                if(selectedFragment!=null){

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();


                }
                else{
                    Log.e(TAG, "Error in creating fragment" );
                }

            }
        });

    }
}