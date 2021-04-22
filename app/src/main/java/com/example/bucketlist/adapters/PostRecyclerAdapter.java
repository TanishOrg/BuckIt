package com.example.bucketlist.adapters;

import android.annotation.SuppressLint;
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
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bucketlist.PostInnerPage;
import com.example.bucketlist.R;
import com.example.bucketlist.model.ActivityModel;
import com.google.android.gms.ads.AdView;
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

import static android.content.ContentValues.TAG;

public class PostRecyclerAdapter extends RecyclerView.Adapter<PostRecyclerAdapter.ViewHolder>
implements Filterable {

    private static final int CONTENT_TYPE = 0;
    public static final int AD_TYPE = 1;
    Context context;
    List modelList;
    String whichActivity;
    List<ActivityModel> modelEgList;

    public PostRecyclerAdapter(Context context, List modelList, String whichActivity) {
        this.context = context;
        this.modelList = modelList;
        this.whichActivity = whichActivity;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        if (viewType == AD_TYPE){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.banner_ad_container, parent, false);
            return new AdviewHolder(view);
        } else  {
            view = LayoutInflater.from(context).inflate(R.layout.activity_row, parent, false);

            this.modelEgList = new ArrayList<>(modelList);
            Log.d("TAG", "PostRecyclerAdapter: " + modelEgList.size());

            return new PostViewHolder(view);
        }


    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder hold, final int position) {

        Object item = modelList.get(position);
        Log.d(TAG, "onBindViewHolder: " + modelList.size());
        if (item instanceof ActivityModel) {
            final PostViewHolder holder = (PostViewHolder) hold;
            ActivityModel i = (ActivityModel) item;
            FirebaseFirestore.getInstance().collection("Users")
                    .document(i.getCreatedByUserID()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (error != null) {
                        Log.e("error", error.getMessage());
                    } else {
                        holder.postedBy.setText(value.getString("Display Name"));
                    }
                }
            });

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");

            sdf.setTimeZone(TimeZone.getDefault());
            String dateAsText = sdf.format(new Date(i.getTimeStamp()).getTime());

            Log.d("datecreated", dateAsText);

            String[] arr = i.getLocation().split(", ", 0);
            String cityFilename = arr[0] + ", " + arr[arr.length - 1];
            holder.location.setText(cityFilename);
            holder.timeCreated.setText(dateAsText);
            holder.title.setText(i.getTitle());
            holder.noOfLikes.setText(Integer.toString(i.getLikes() - i.getDislikes()));
            holder.totalComments.setText(Integer.toString(i.getTotalComments()) + " comments");

            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent in = new Intent(context, PostInnerPage.class);
//                    Log.d("123id", i.getPostId());
                    in.putExtra("postId",((ActivityModel) modelList.get(position)).getPostId());
                    switch (whichActivity) {
                        case "city fragment":
                            in.putExtra("to activity", "city fragment");
                            break;
                        case "city inner page":
                            in.putExtra("to activity", "city inner page");
                            break;
                        case "bookmark page":
                            in.putExtra("to activity", "bookmark page");
                            break;
                        case "my post page":
                            in.putExtra("to activity", "my post page");
                            break;
                        case "see more post page":
                            in.putExtra("to activity", "see more post page");
                            break;
                    }
                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(in);
                }
            });

        }else {
            AdView adView =(AdView) modelList.get(position);
            AdviewHolder adviewHolder = (AdviewHolder) hold;

            ViewGroup adCardView =(ViewGroup) adviewHolder.itemView;
            Log.d(TAG, "onBindViewHolder: "  + adView.toString());

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
        if (modelList.get(position) instanceof AdView)
                return AD_TYPE;
        else return CONTENT_TYPE;
    }

    @Override
    public int getItemCount() {
        return modelList != null ? modelList.size() : 0;
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private  Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List filterd = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filterd.addAll(modelEgList);
            } else {
                String patttern = constraint.toString().toLowerCase().trim();

                for (Object i : modelEgList) {
                    if(i instanceof ActivityModel) {
                        ActivityModel item = (ActivityModel) i;
                        if (item.getTitle().toLowerCase().contains(patttern)
                                || (item.getCategory() != null && item.getCategory().toString()
                                .toLowerCase().contains(patttern))
                                || item.getLocation().toLowerCase().contains(patttern)
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
        protected void publishResults(CharSequence constraint, FilterResults results) {
            modelList.clear();
            modelList.addAll((List) results.values);

            notifyDataSetChanged();
        }
    };

    public abstract class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    private class AdviewHolder extends ViewHolder {
        public AdviewHolder(@NonNull final View itemView) {
            super(itemView);
        }
    }

    class PostViewHolder extends ViewHolder{

        TextView postedBy,location,timeCreated,title,noOfLikes,totalComments;
        ImageView likeButton;
        CardView cardView;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);

            postedBy = itemView.findViewById(R.id.postedBy);
            location = itemView.findViewById(R.id.location);
            timeCreated = itemView.findViewById(R.id.timeCreated);
            title = itemView.findViewById(R.id.title);
            likeButton = itemView.findViewById(R.id.likeButton);
            noOfLikes = itemView.findViewById(R.id.noOfLikes);
            cardView = itemView.findViewById(R.id.cardView);
            totalComments = itemView.findViewById(R.id.totalComments);
        }
    }
}
