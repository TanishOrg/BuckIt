package com.example.bucketlist.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bucketlist.R;
import com.example.bucketlist.model.CommentModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerAdapterComment extends RecyclerView.Adapter<RecyclerAdapterComment.CommentViewHolder>
        {
    private static final String TAG = "Likes";
    Context context;
    List<CommentModel> commentModelList;
    FirebaseFirestore firestore;
    String postid;

    public RecyclerAdapterComment(Context context, List<CommentModel> commentModelList,String postid) {
        this.context = context;
        this.commentModelList = commentModelList;
        firestore = FirebaseFirestore.getInstance();
        this.postid = postid;
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
    public void onBindViewHolder(@NonNull final CommentViewHolder holder, final int position) {

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

        /**
         * check if likes exists or not for the user
         */
        firestore.collection("Posts").document(postid)
                .collection("Comments").document(commentModelList.get(position).getCommentId())
                .collection("likedBy")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if (task.isSuccessful()) {
                            DocumentSnapshot snapshot = task.getResult();
                            if (snapshot.exists()) {
                                holder.isLiked = true;
                                Log.d(TAG, "onComplete: " + holder.isLiked);
                                holder.likesText.setTextColor(Color.parseColor("#FF0000"));
                            }
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
//        holder.likesText.setOnClickListener(this);
        Log.d("datecreated",dateAsText);

        holder.likesText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Map map1 = new HashMap();
                map1.put("ref", firestore.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()));
                DocumentReference reference = firestore.collection("Posts").document(postid)
                        .collection("Comments").document(commentModelList.get(position).getCommentId())
                        .collection("likedBy")
                        .document(FirebaseAuth.getInstance().getCurrentUser().getUid());


                DocumentReference documentReference = firestore.collection("Posts").document(postid)
                        .collection("Comments").document(commentModelList.get(position).getCommentId());

                if (holder.isLiked == false) {

                    reference.set(map1);
                    documentReference
                            .update("total likes", commentModelList.get(position).getLikes() + 1)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    holder.isLiked = true;
                                }
                            });
                } else  {
                    reference.delete();
                    documentReference
                            .update("total likes", commentModelList.get(position).getLikes() - 1)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    holder.isLiked = false;
                                    holder.likesText.setTextColor(Color.parseColor("#000000"));
                                }
                            });
                }
            }
        });

    }




    class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView username;
        TextView comment;
        TextView timecreated;
        TextView noOfLikes;
        TextView likesText;
        CircleImageView userimage;
        boolean isLiked = false;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            userimage = itemView.findViewById(R.id.userImage);
            username = itemView.findViewById(R.id.username);
            comment = itemView.findViewById(R.id.comment);
            timecreated = itemView.findViewById(R.id.timecreated);
            noOfLikes = itemView.findViewById(R.id.noOfLikes);
            likesText = itemView.findViewById(R.id.likes);
            
        }

        
    }
}
