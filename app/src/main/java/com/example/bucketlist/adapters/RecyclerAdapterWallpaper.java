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
import com.example.bucketlist.EditItem;
import com.example.bucketlist.PopUpShowItem;
import com.example.bucketlist.R;
import com.example.bucketlist.model.BucketItems;
import com.example.bucketlist.model.WallpaperModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.List;

public class RecyclerAdapterWallpaper extends RecyclerView.Adapter<WallpaperViewHolder>  {

    private Context context;
    private List<WallpaperModel> wallpaperModelList;
    public int selectedImagePosition = -1;
    public String selectedImageUrl;
    Activity activity;

    public RecyclerAdapterWallpaper(Context context, List<WallpaperModel> wallpaperModelList, Activity activity) {
        this.context = context;
        this.wallpaperModelList = wallpaperModelList;
        this.activity = activity;
    }


    @NonNull
    @Override
    public WallpaperViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wallpaper_item,parent,false);
        return new WallpaperViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final WallpaperViewHolder holder, int position) {

        Glide.with(context).load(wallpaperModelList.get(position).getRawUrl()).into(holder.wallpaper);

        if(position==selectedImagePosition){
            holder.selectedImage.setVisibility(View.VISIBLE);
            selectedImageUrl = wallpaperModelList.get(position).getRawUrl();
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


}

class WallpaperViewHolder extends RecyclerView.ViewHolder{

    CardView wallpaperCard;
    ImageView wallpaper;
    ImageView selectedImage;
    public WallpaperViewHolder(@NonNull View itemView) {
        super(itemView);
        wallpaperCard = itemView.findViewById(R.id.wallpaperCard);
        wallpaper = itemView.findViewById(R.id.wallpaper);
        selectedImage = itemView.findViewById(R.id.selectedImage);
    }
}
