package com.example.bucketlist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;

import com.example.bucketlist.fragment.AddFragment;
import com.example.bucketlist.fragment.CityFragment;
import com.example.bucketlist.fragment.ProfileFragment;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = HomeActivity.class.getSimpleName() ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ChipNavigationBar bottomNav = findViewById(R.id.bottom_nav);
       FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();

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