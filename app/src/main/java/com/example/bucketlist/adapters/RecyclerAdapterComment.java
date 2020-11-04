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

import com.bumptech.glide.Glide;
import com.example.bucketlist.R;
import com.example.bucketlist.model.CommentModel;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerAdapterComment extends RecyclerView.Adapter<RecyclerAdapterComment.CommentViewHolder>{
    Context context;
    List<CommentModel> commentModelList;

    public RecyclerAdapterComment(Context context, List<CommentModel> commentModelList) {
        this.context = context;
        this.commentModelList = commentModelList;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.comment_item,parent,false);
        return new CommentViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return commentModelList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull final CommentViewHolder holder, int position) {
        holder.comment.setText(commentModelList.get(position).getComment());
        holder.noOfLikes.setText(Integer.toString(commentModelList.get(position).getLikes()));
        FirebaseFirestore.getInstance().collection("Users")
                .document(commentModelList.get(position).getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error!=null){
                    Log.e("error",error.getMessage());
                }
                else {
                    holder.username.setText(value.getString("Display Name"));
                    Glide.with(context).load(value.getString("Image Uri")).into(holder.userimage);
                }
            }
        });

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");

        sdf.setTimeZone(TimeZone.getDefault());

        String dateAsText = sdf.format(new Date(commentModelList.get(position).getTimeStamp()).getTime());

        Log.d("timezone123",TimeZone.getDefault().toString());
        holder.timecreated.setText(dateAsText);

        Log.d("datecreated",dateAsText);


    }



    class CommentViewHolder extends RecyclerView.ViewHolder{
        TextView username,comment,timecreated,noOfLikes;
        CircleImageView userimage;
        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            userimage = itemView.findViewById(R.id.userImage);
            username = itemView.findViewById(R.id.username);
            comment = itemView.findViewById(R.id.comment);
            timecreated = itemView.findViewById(R.id.timecreated);
            noOfLikes = itemView.findViewById(R.id.noOfLikes);
        }

    }
}
