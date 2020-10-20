package com.example.bucketlist.adapters;

import android.content.Context;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.bucketlist.R;
import com.example.bucketlist.model.CityModel;

import java.text.BreakIterator;
import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.viewHolder> {

    Context context;
    ArrayList<CityModel> arrayList;

    public CustomAdapter(Context context, ArrayList<CityModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public  CustomAdapter.viewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.city_item, viewGroup, false);
        return new viewHolder(view);
    }
    @Override
    public  void onBindViewHolder(CustomAdapter.viewHolder viewHolder,int position) {
        viewHolder.city_name.setText(arrayList.get(position).getCity());
        viewHolder.imageView.setImageResource(arrayList.get(position).getImage());

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView city_name;

        public viewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageCity);
            city_name = (TextView) itemView.findViewById(R.id.city_name);

        }
    }

}

//import android.content.Context;
//import android.content.Intent;
//import androidx.annotation.NonNull;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.fragment.app.Fragment;
//import androidx.fragment.app.FragmentManager;
//import androidx.fragment.app.FragmentPagerAdapter;
//import androidx.viewpager.widget.PagerAdapter;
//
//import com.bumptech.glide.load.model.Model;
//import com.example.bucketlist.R;
//import com.example.bucketlist.model.CityModel;
//
//import java.util.List;
//
//public class cityAdapter extends PagerAdapter {
//
//    private List<CityModel> models;
//    private LayoutInflater layoutInflater;
//    private Context context;
//
//    public cityAdapter(List<CityModel> models, Context context) {
//
//        this.models = models;
//        this.context = context;
//    }
//
//    @Override
//    public int getCount() {
//        return models.size();
//    }
//
//    @Override
//    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
//        return view.equals(object);
//    }
//
//
//    @NonNull
//    @Override
//    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
//        layoutInflater = LayoutInflater.from(context);
//        View view = layoutInflater.inflate(R.layout.city_item, container, false);
//
//        ImageView imageView;
//        TextView city , country;
//
//        imageView = view.findViewById(R.id.image);
//        city = view.findViewById(R.id.city_title);
//        country = view.findViewById(R.id.country);
//
//        imageView.setImageResource(models.get(position).getImage());
//        city.setText(models.get(position).getCity());
//        country.setText(models.get(position).getCountry());
//
////        view.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                Intent intent = new Intent(context, DetailActivity.class);
////                intent.putExtra("param", models.get(position).getCity());
////                context.startActivity(intent);
////                // finish();
////            }
////        });
//
//        container.addView(view, 0);
//        return view;
//    }
//
//    @Override
//    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
//        container.removeView((View)object);
//    }
//}

