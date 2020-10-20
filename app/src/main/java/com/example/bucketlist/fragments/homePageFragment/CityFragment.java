package com.example.bucketlist.fragments.homePageFragment;

import android.animation.ArgbEvaluator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.load.model.Model;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.bucketlist.R;
import com.example.bucketlist.adapters.CustomAdapter;
import com.example.bucketlist.model.CityModel;

import java.util.ArrayList;
import java.util.List;


public class CityFragment extends Fragment {

    private View view;
    ArrayList<CityModel> arrayList;
    RecyclerView recyclerView;
    int images[] = {R.drawable.athens,R.drawable.colombo,R.drawable.london,R.drawable.pisa};
    String cityName[] = {"Athens", "Colombo", "London", "Pisa"};


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

       view= inflater.inflate(R.layout.fragment_city,container,false);

       ImageSlider imageSlider= (ImageSlider)view.findViewById(R.id.slider);

       List<SlideModel> slideModels=new ArrayList<>();

       slideModels.add(new SlideModel(R.drawable.sliderbg1));
        slideModels.add(new SlideModel(R.drawable.sliderbg2));
        slideModels.add(new SlideModel(R.drawable.sliderbg3));
        imageSlider.setImageList(slideModels,true);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        arrayList = new ArrayList<>();

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        for (int i = 0; i < images.length; i++) {
            CityModel cityModel = new CityModel();
            cityModel.setImage(images[i]);
            cityModel.setCity(cityName[i]);

            //add in array list
            arrayList.add(cityModel);
        }

        CustomAdapter adapter = new CustomAdapter(getContext(), arrayList);
        recyclerView.setAdapter(adapter);

        return  view;
    }
}
