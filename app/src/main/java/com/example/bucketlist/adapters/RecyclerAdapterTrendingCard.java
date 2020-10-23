package com.example.bucketlist.adapters;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bucketlist.R;
import com.example.bucketlist.model.CityModel;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapterTrendingCard extends RecyclerView.Adapter<RecyclerAdapterTrendingCard.viewHolder> {

    Context context;
    List<CityModel> arrayList;

    public RecyclerAdapterTrendingCard(Context context, List<CityModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public  RecyclerAdapterTrendingCard.viewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.mainpagecity_item, viewGroup, false);
        return new viewHolder(view);
    }
    @Override
    public  void onBindViewHolder(RecyclerAdapterTrendingCard.viewHolder viewHolder, int position) {
        viewHolder.city_name.setText(arrayList.get(position).getCity());
        viewHolder.country_name.setText(arrayList.get(position).getCountry());
        Log.d("name",viewHolder.city_name.getText().toString());
        Glide.with(context).load(arrayList.get(position).getImage()).into(viewHolder.imageView);


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView city_name,country_name;

        public viewHolder(View itemView) {
            super(itemView);


            imageView = itemView.findViewById(R.id.imageCity);
            city_name = itemView.findViewById(R.id.city_name);
            country_name = itemView.findViewById(R.id.county_name);

        }
    }

}

