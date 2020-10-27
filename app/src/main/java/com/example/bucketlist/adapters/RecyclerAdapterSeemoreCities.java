package com.example.bucketlist.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bucketlist.CityInnerPage;
import com.example.bucketlist.R;
import com.example.bucketlist.model.CityModel;

import java.util.List;

public class RecyclerAdapterSeemoreCities extends RecyclerView.Adapter<RecyclerAdapterSeemoreCities.viewHolder> {
    Context context;
    List<CityModel> cityModelList;

    public RecyclerAdapterSeemoreCities(Context context, List<CityModel> cityModelList) {
        this.context = context;
        this.cityModelList = cityModelList;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.see_more_cities_item,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapterSeemoreCities.viewHolder viewHolder, final int position) {
        viewHolder.city_name.setText(cityModelList.get(position).getCity());
        viewHolder.country_name.setText(cityModelList.get(position).getCountry());
        Log.d("name",viewHolder.city_name.getText().toString());
        Glide.with(context).load(cityModelList.get(position).getImage()).into(viewHolder.imageView);
        Log.d("id",cityModelList.get(position).getStringId());

        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), CityInnerPage.class);
                i.putExtra("cityId",cityModelList.get(position).getStringId());
                view.getContext().startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return cityModelList.size();
    }

    class viewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView city_name,country_name;
        CardView cardView;
        public viewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.backgroundImage);
            city_name = itemView.findViewById(R.id.cityName);
            country_name = itemView.findViewById(R.id.countryName);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
