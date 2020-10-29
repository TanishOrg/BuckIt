package com.example.bucketlist.adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bucketlist.CityInnerPage;
import com.example.bucketlist.R;
import com.example.bucketlist.model.WallpaperModel;

import java.util.List;

public class RecyclerViewChangeWallpaper extends RecyclerView.Adapter<RecyclerViewChangeWallpaper.ChangeWallpaperViewHolder> {
    private Context context;
    private List<WallpaperModel> wallpaperModelList;
    public int selectedImagePosition = -1;
    public String selectedImageUrl;
    CityInnerPage cityInnerPage;

    public RecyclerViewChangeWallpaper(Context context, List<WallpaperModel> wallpaperModelList, CityInnerPage cityInnerPage) {
        this.context = context;
        this.wallpaperModelList = wallpaperModelList;
        this.cityInnerPage = cityInnerPage;
    }

    @NonNull
    @Override
    public ChangeWallpaperViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wallpaper_item,parent,false);
        return new ChangeWallpaperViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ChangeWallpaperViewHolder holder, int position) {
        Glide.with(context).load(wallpaperModelList.get(position).getRawUrl()).into(holder.wallpaper);

        if(position==selectedImagePosition){
            holder.selectedImage.setVisibility(View.VISIBLE);
            selectedImageUrl = wallpaperModelList.get(position).getRawUrl();
            Glide.with(context).load(selectedImageUrl).into(cityInnerPage.backgroundImage);
            Log.d("selected Image url", selectedImageUrl);
        }
        else {
            holder.selectedImage.setVisibility(View.GONE);
        }



        holder.wallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedImagePosition = holder.getBindingAdapterPosition();
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return wallpaperModelList.size();
    }

    class ChangeWallpaperViewHolder extends RecyclerView.ViewHolder{
        CardView wallpaperCard;
        ImageView wallpaper;
        ImageView selectedImage;

    public ChangeWallpaperViewHolder(@NonNull View itemView) {
        super(itemView);
        wallpaperCard = itemView.findViewById(R.id.wallpaperCard);
        wallpaper = itemView.findViewById(R.id.wallpaper);
        selectedImage = itemView.findViewById(R.id.selectedImage);
    }
}
}
