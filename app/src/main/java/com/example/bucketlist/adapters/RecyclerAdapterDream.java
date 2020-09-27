package com.example.bucketlist.adapters;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bucketlist.R;
import com.example.bucketlist.model.BucketItemModify;
import com.example.bucketlist.model.BucketItems;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;


public class RecyclerAdapterDream extends RecyclerView.Adapter<RecyclerAdapterDream.ViewHolder>{

    private static final String TAG = "RECYCLERadapter";
    private Context context;
    private List<BucketItems> itemsList;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private BucketItemModify itemModify;

    public RecyclerAdapterDream(Context context, List<BucketItems> items, BucketItemModify modify) {
        this.context = context;
        this.itemsList = items;
        this.itemModify = modify;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mAuth = FirebaseAuth.getInstance();
        if (mAuth != null){
            mUser = mAuth.getCurrentUser();
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_dream,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final BucketItems items = itemsList.get(position);
        holder.id = items.getId();
        holder.cardTitle.setText(items.getTitle());
        holder.cardTargetDate.setText(items.getDeadline());

        bindHolder(holder,items,position);
    }

    @Override
    public int getItemCount() {
        return itemsList != null ? itemsList.size() : 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        @NonNull
        Dialog myDialog;
        RelativeLayout card_item;
        ImageView imageView;
        TextView cardTitle , cardTargetDate;
        int id;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.cardCategoryIcon);
            cardTitle = itemView.findViewById(R.id.cardTitle);
            cardTargetDate = itemView.findViewById(R.id.cardTargetDate);
            card_item = itemView.findViewById(R.id.card_item);

            //inflating
            myDialog = new Dialog(context,android.R.style.Theme_Translucent_NoTitleBar);
            myDialog.setContentView(R.layout.popup_show_window);

        }
    }

    private void updateData(BucketItems items, final ViewHolder holder, final int position) {
        FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
        DocumentReference documentReference = fireStore.collection("Users").document(mUser.getUid())
                .collection("items").document(items.getStringID());
        documentReference.update(items.toHashMap()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "onSuccess: Sucess" );
                itemsList.remove(position);
                itemModify.onItemDeleted();
                notifyDataSetChanged();
                holder.myDialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: " + e.getMessage());
            }
        });
    }

    private void bindHolder(final ViewHolder holder, final BucketItems items, final int position) {
        TextView titleOfCard = holder.myDialog.findViewById(R.id.cardTitle);
        TextView infoOfCard = holder.myDialog.findViewById(R.id.cardDescription);
        TextView targetOfCard = holder.myDialog.findViewById(R.id.card_target_date);
        Button completedButton = holder.myDialog.findViewById(R.id.completeButton);
        ImageView privacyImageView = holder.myDialog.findViewById(R.id.privacyImageView);

        titleOfCard.setText(items.getTitle());
        if (items.getInfo() != null) {
            infoOfCard.setText(items.getInfo());
        }
        completedButton.setText(items.isAchieved() ? "Completed": "Not Completed");
        privacyImageView.setImageResource(items.isPrivate()? R.drawable.ic_baseline_person_24: R.drawable.ic_baseline_public_24);
        targetOfCard.setText(items.getDeadline());
        holder.card_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "test click" + String.valueOf(holder.getAdapterPosition()), Toast.LENGTH_SHORT).show();
                holder.myDialog.show();
            }
        });

        completedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //todo add this to achieved list
                items.setAchieved(items.isAchieved() ? false:true);
                updateData(items,holder,position);
            }
        });

    }


}
