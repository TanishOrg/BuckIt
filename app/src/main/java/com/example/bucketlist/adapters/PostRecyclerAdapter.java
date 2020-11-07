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

public class PostRecyclerAdapter extends RecyclerView.Adapter<PostRecyclerAdapter.PostViewHolder>
implements Filterable {

    Context context;
    List<ActivityModel> modelList;
    String whichActivity;
    List<ActivityModel> modelEgList;

    public PostRecyclerAdapter(Context context, List<ActivityModel> modelList, String whichActivity) {
        this.context = context;
        this.modelList = modelList;
        this.whichActivity = whichActivity;

    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_row, parent, false);

        this.modelEgList= new ArrayList<>( modelList );
        Log.d("TAG", "PostRecyclerAdapter: " + modelEgList.size());

        return new PostViewHolder(view);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final PostViewHolder holder, final int position) {

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
        holder.noOfLikes.setText(Integer.toString( modelList.get(position).getLikes() - modelList.get(position).getDislikes()) );
        holder.totalComments.setText(Integer.toString(modelList.get(position).getTotalComments()) + " comments");

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, PostInnerPage.class);
                Log.d("123id",modelList.get(position).getPostId());
                i.putExtra("postId",modelList.get(position).getPostId());
                switch (whichActivity){
                    case "city fragment":
                        i.putExtra("to activity","city fragment");
                        break;
                    case "city inner page":
                        i.putExtra("to activity","city inner page");
                        break;
                    case "bookmark page":
                        i.putExtra("to activity","bookmark page");
                        break;
                    case "my post page":
                        i.putExtra("to activity","my post page");
                        break;
                    case "see more post page":
                        i.putExtra("to activity","see more post page");
                        break;
                }
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });



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
            List<ActivityModel> filterd = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filterd.addAll(modelEgList);
            } else {
                String patttern = constraint.toString().toLowerCase().trim();

                for (ActivityModel item : modelEgList) {
                    if (item.getTitle().toLowerCase().contains(patttern)
                            || (item.getCategory() != null && item.getCategory().toString()
                            .toLowerCase().contains(patttern) )
                            || item.getLocation().toLowerCase().contains(patttern)
                    ) {
                        filterd.add(item);
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

    class PostViewHolder extends RecyclerView.ViewHolder{

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
