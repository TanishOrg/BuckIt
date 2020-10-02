package com.example.bucketlist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.bucketlist.fragments.homePageFragment.AddFragment;
import com.example.bucketlist.fragments.homePageFragment.CityFragment;
import com.example.bucketlist.fragments.homePageFragment.ProfileFragment;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = HomeActivity.class.getSimpleName() ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

      //  getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_FULLSCREEN);


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