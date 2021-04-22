package com.example.bucketlist.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bucketlist.CityInnerPage;
import com.example.bucketlist.R;
import com.example.bucketlist.model.ActivityModel;
import com.example.bucketlist.model.CityModel;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class RecyclerAdapterSeemoreCities extends RecyclerView.Adapter<RecyclerAdapterSeemoreCities.ViewHolder>
implements Filterable {
    private static final int CONTENT_CODE = 0 ;
    private static final int AD_CODE = 1;
    Context context;
    List cityModelList;
    List modelEgList;
    public RecyclerAdapterSeemoreCities(Context context, List<CityModel> cityModelList) {
        this.context = context;
        this.cityModelList = cityModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view ;

        if (viewType == CONTENT_CODE) {
            view = LayoutInflater.from(context).inflate(R.layout.see_more_cities_item,parent,false);
            this.modelEgList= new ArrayList<>( cityModelList );
            return new ContentViewHolder(view);
        } else  {
            Log.d(TAG, "onCreateViewHolder: ADTYPE");
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.banner_ad_container, parent, false);


            return new AdViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapterSeemoreCities.ViewHolder holder, final int position) {
        Object i = cityModelList.get(position);

        if (i instanceof CityModel) {

            ContentViewHolder viewHolder = (ContentViewHolder) holder;
            CityModel item = (CityModel) cityModelList.get(position);
            viewHolder.city_name.setText(item.getCity());
            viewHolder.country_name.setText(item.getCountry());
            Log.d("name",viewHolder.city_name.getText().toString());
            Glide.with(context).load(item.getImage()).into(viewHolder.imageView);
            Log.d("id",item.getStringId());

            viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(view.getContext(), CityInnerPage.class);
                    i.putExtra("cityId",((CityModel) cityModelList.get(position)).getStringId());
                    view.getContext().startActivity(i);
                }
            });

        } else  {
            AdView adView =(AdView) cityModelList.get(position);
            AdViewHolder adviewHolder = (AdViewHolder) holder;

            ViewGroup adCardView =(ViewGroup) adviewHolder.itemView;
            Log.d(TAG, "onBindViewHolder: Hello "  + adView.toString());

            if (adCardView.getChildCount() > 0) {
                adCardView.removeAllViews();
            }

            if (adCardView.getParent() != null) {
                ((ViewGroup) adView.getParent()).removeView(adView);
            }

            adCardView.addView(adView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (cityModelList.get(position) instanceof CityModel){
            return CONTENT_CODE;
        } else {
            Log.d(TAG, "getItemViewType: ADTYPE");
            return AD_CODE;
        }
    }

    @Override
    public int getItemCount() {
        return cityModelList != null ? cityModelList.size() : 0;
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private  Filter filter = new Filter(){

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List filterd = new ArrayList<>();
            if (charSequence == null || charSequence.length() == 0) {
                filterd.addAll(modelEgList);
            }else {
                String patttern = charSequence.toString().toLowerCase().trim();

                for (Object i : modelEgList) {
                    if (i instanceof CityModel){
                        CityModel item = (CityModel) i;
                        if (item.getCity().toLowerCase().contains(patttern)
                                || (item.getCountry() != null && item.getCountry()
                                .toLowerCase().contains(patttern) )
                                || item.getStringId().toLowerCase().contains(patttern)
                        ) {
                            filterd.add(item);
                        }
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values  = filterd;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            cityModelList.clear();
            cityModelList.addAll((List) filterResults.values);

            notifyDataSetChanged();
        }
    };

    class ViewHolder extends  RecyclerView.ViewHolder{

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    class AdViewHolder extends  ViewHolder{

        public AdViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
    class ContentViewHolder extends ViewHolder{

        ImageView imageView;
        TextView city_name,country_name;
        CardView cardView;
        public ContentViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.backgroundImage);
            city_name = itemView.findViewById(R.id.cityName);
            country_name = itemView.findViewById(R.id.countryName);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
