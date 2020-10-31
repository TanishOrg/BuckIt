package com.example.bucketlist.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bucketlist.R;
import com.example.bucketlist.model.ActivityModel;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

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
    public void onBindViewHolder(@NonNull final PostViewHolder holder, int position) {

        FirebaseFirestore.getInstance().collection("Users")
                .document(modelList.get(position).getCreatedByUserID()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error!=null){
                    Log.e("error",error.getMessage());
                }
                else {
                    holder.postedBy.setText(value.getString("Display Name"));
                }
            }
        });

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");

        sdf.setTimeZone(TimeZone.getDefault());

        Log.d("timestamp",Long.toString(modelList.get(position).getTimeStamp()));
        Log.d("timezone",TimeZone.getDefault().toString());
        String dateAsText = sdf.format(new Date(modelList.get(position).getTimeStamp()).getTime());

        Log.d("datecreated",dateAsText);

        String[] arr = modelList.get(position).getLocation().split(", ",0);
        String cityFilename = arr[0] + ", " + arr[arr.length - 1];
        holder.location.setText(cityFilename);
        holder.timeCreated.setText(dateAsText);
        holder.title.setText(modelList.get(position).getTitle());
        holder.noOfLikes.setText(Integer.toString( modelList.get(position).getLikes()));



    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    class PostViewHolder extends RecyclerView.ViewHolder{

        TextView postedBy,location,timeCreated,title,noOfLikes;
        ImageView likeButton;
        public PostViewHolder(@NonNull View itemView) {
            super(itemView);

            postedBy = itemView.findViewById(R.id.postedBy);
            location = itemView.findViewById(R.id.location);
            timeCreated = itemView.findViewById(R.id.timeCreated);
            title = itemView.findViewById(R.id.title);
            likeButton = itemView.findViewById(R.id.likeButton);
            noOfLikes = itemView.findViewById(R.id.noOfLikes);
        }
    }
}
