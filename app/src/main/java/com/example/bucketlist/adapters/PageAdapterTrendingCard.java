package com.example.bucketlist.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.bucketlist.CityInnerPage;
import com.example.bucketlist.R;
import com.example.bucketlist.fragments.homePageFragment.CityFragment;
import com.example.bucketlist.model.TrendingCardModel;

import java.util.ArrayList;
import java.util.List;

public class PageAdapterTrendingCard extends PagerAdapter {

    private Context context;
    private List<TrendingCardModel> trendingCardModelArrayList;

    public PageAdapterTrendingCard(Context context, List<TrendingCardModel> trendingCardModelArrayList) {
        this.context = context;
        this.trendingCardModelArrayList = trendingCardModelArrayList;
    }

    @Override
    public int getCount() {
        return trendingCardModelArrayList != null ? trendingCardModelArrayList.size() : 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {

        View view = LayoutInflater.from(context).inflate(R.layout.trending_card_item,container,false);

        ImageView backgroundImage = view.findViewById(R.id.backgroundImage);
        final TextView city = view.findViewById(R.id.city);
        TextView country = view.findViewById(R.id.country);

        TrendingCardModel trendingCardModel = trendingCardModelArrayList.get(position);
        final String cityName = trendingCardModel.getCity();
        final String countryName = trendingCardModel.getCountry();
        String imageUrl = trendingCardModel.getBackgroundImageUrl();


        Glide.with(context).load(imageUrl).into(backgroundImage);
        city.setText(cityName);
        country.setText(countryName);


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             // Toast.makeText(context, position+cityName+countryName, Toast.LENGTH_SHORT).show();
                Intent i = new Intent(view.getContext(), CityInnerPage.class);
                i.putExtra("cityId",trendingCardModelArrayList.get(position).getId());
                view.getContext().startActivity(i);

            }
        });


        container.addView(view);
        return view;

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);

    }
}
