package com.example.bucketlist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;

import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class MainActivity2 extends AppCompatActivity {

    private static final String TAG =MainActivity2.class.getSimpleName() ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        ChipNavigationBar bottomNav = findViewById(R.id.bottom_nav);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
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