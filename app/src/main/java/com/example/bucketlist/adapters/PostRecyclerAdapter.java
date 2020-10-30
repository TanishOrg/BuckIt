package com.example.bucketlist.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bucketlist.R;
import com.example.bucketlist.model.ActivityModel;

import java.util.List;

public class PostRecyclerAdapter extends RecyclerView.Adapter<PostRecyclerAdapter.PostViewHolder> {

    Context context;
    List<ActivityModel> modelList;

    public PostRecyclerAdapter(Context context, List<ActivityModel> modelList) {
        this.context = context;
        this.modelList = modelList;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_row, parent, false);
        return new PostViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        holder.postedBy.setText(modelList.get(position).getCreatedByUserID());
        holder.location.setText(modelList.get(position).getLocation());
        holder.timeCreated.setText(Integer.toString((int) modelList.get(position).getTimeStamp()));
        holder.title.setText(modelList.get(position).getTitle());


    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    class PostViewHolder extends RecyclerView.ViewHolder{

        TextView postedBy,location,timeCreated,title;
        ImageView likeButton;
        public PostViewHolder(@NonNull View itemView) {
            super(itemView);

            postedBy = itemView.findViewById(R.id.postedBy);
            location = itemView.findViewById(R.id.location);
            timeCreated = itemView.findViewById(R.id.timeCreated);
            title = itemView.findViewById(R.id.title);
            likeButton = itemView.findViewById(R.id.likeButton);
        }
    }
}
