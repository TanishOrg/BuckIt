package com.example.bucketlist.adapters;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bucketlist.EditItem;
import com.example.bucketlist.PopUpShowItem;
import com.example.bucketlist.R;
import com.example.bucketlist.model.BucketItemModify;
import com.example.bucketlist.model.BucketItems;
import com.example.bucketlist.model.ItemAdapter;
import com.example.bucketlist.model.OnItemDelete;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
//import com.vansuita.gaussianblur.GaussianBlur;

import java.util.List;


public class RecyclerAdapterAchieved extends ItemAdapter<RecyclerAdapterAchieved.ViewHolder> {
//        extends RecyclerView.Adapter<RecyclerAdapterAchieved.ViewHolder>{


    Context context;
    private BucketItemModify bucketItemModify;
    List<BucketItems> itemsList;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    private static final String TAG = "RECYCLERadapter";


    public RecyclerAdapterAchieved(Context context, List<BucketItems> items, BucketItemModify modify) {
        this.context = context;
        this.itemsList = items;
        this.bucketItemModify = modify;
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
        String category = items.getCategory();
        holder.backgroundiconAchieved.setImageResource(R.drawable.ic_back_to_active);
        holder.cardBackground.setClipToOutline(true);
        switch (category){
            case "Travel":
                holder.categoryImageView.setImageResource(R.drawable.ic_baseline_flight_24);
                holder.cardBackground.setImageResource(R.mipmap.travelbackground);

//                GaussianBlur.with(context).put(R.mipmap.travelbackground,holder.cardBackground);
                break;
            case "Adventure":
                holder.categoryImageView.setImageResource(R.drawable.ic_backpack);
                holder.cardBackground.setImageResource(R.mipmap.adventurebackground);
                break;
            case "Food":
                holder.categoryImageView.setImageResource(R.drawable.ic_hamburger);
                holder.cardBackground.setImageResource(R.mipmap.foodbackground);
                break;
            case "Relation":
                holder.categoryImageView.setImageResource(R.drawable.ic_heart);
                holder.cardBackground.setImageResource(R.mipmap.relationbackground);
                break;
            case "Career":
                holder.categoryImageView.setImageResource(R.drawable.ic_portfolio);
                holder.cardBackground.setImageResource(R.mipmap.careerbackground);
                break;
            case "Financial":
                holder.categoryImageView.setImageResource(R.drawable.ic_financial);
                holder.cardBackground.setImageResource(R.mipmap.financialbackground);
                break;
            case "Learning":
                holder.categoryImageView.setImageResource(R.drawable.ic_reading_book);
                holder.cardBackground.setImageResource(R.mipmap.learningbackground);
                break;
            case "Health":
                holder.categoryImageView.setImageResource(R.drawable.ic_health);
                holder.cardBackground.setImageResource(R.mipmap.healthbackground);
                break;
            case "Other":
                holder.categoryImageView.setImageResource(R.drawable.ic_menu);
                break;

        }

        bindHolder(holder,items,position);
    }

    @Override
    public int getItemCount() {
        return itemsList != null ? itemsList.size() : 0;
    }

    @Override
    protected void bindHolder(final ViewHolder holder, final BucketItems items, final int position) {

        new PopUpShowItem(context, items, mUser, holder.myDialog, false) {
            @Override
            protected void onCompleteButtonClicked() {
                items.setAchieved(items.isAchieved() ? false:true);
                updateData(items,holder,position);
            }

            @Override
            protected void onEditButtonClick() {
                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context,R.style.BottomSheetDialogTheme);
                new EditItem(context, items, mUser, bottomSheetDialog) {
                    @Override
                    protected void onEditComplete() {
                        notifyDataSetChanged();
                    }
                };
            }

        }.hideEditButton();

        holder.card_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(context, "test click" + String.valueOf(holder.getAdapterPosition()), Toast.LENGTH_SHORT).show();
                holder.myDialog.show();
            }
        });

    }

    @Override
    public void deleteItem(final int position, final ViewHolder viewHolder, final OnItemDelete onItemDelete) {
        Log.d(TAG, "deleteItem: ");
        new AlertDialog.Builder(context).setTitle("Are You Sure")
                .setCancelable(true)
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        notifyDataSetChanged();
                    }
                })
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Snackbar.make(viewHolder.itemView,"Deleted",100).show();
                        deleteFromFireBase(position,viewHolder,onItemDelete);
                        notifyDataSetChanged();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Snackbar.make(viewHolder.itemView,"Cancelled",300).show();
                        notifyDataSetChanged();
                    }
                })
                .create()
                .show();
    }

    @Override
    public void moveItem(int position, ViewHolder viewHolder) {
        BucketItems items = itemsList.get(position);
        items.setAchieved(items.isAchieved() ? false:true);
        updateData(items,viewHolder,position);
    }

    private void deleteFromFireBase(final int position, final RecyclerAdapterAchieved.ViewHolder viewHolder, final OnItemDelete onItemDelete) {
        final BucketItems item = itemsList.get(position);
        FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
        DocumentReference documentReference = fireStore.collection("Users").document(mUser.getUid())
                .collection("items").document(item.getStringID());
        documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
//                Snackbar.make(viewHolder.itemView,"Sucess",300).show();
//                itemsList.remove(position);
//                onItemDelete.refreshFragment();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar.make(viewHolder.itemView,e.toString(),300).show();
                    }
                });
    }

    @Override
    protected void updateData(BucketItems items, final ViewHolder holder, final int position) {
        FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
        DocumentReference documentReference = fireStore.collection("Users").document(mUser.getUid())
                .collection("items").document(items.getStringID());
        documentReference.update(items.toHashMap()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "onSuccess: Sucess");
//                itemsList.remove(position);
//                bucketItemModify.onItemDeleted();
//                notifyDataSetChanged();
                holder.myDialog.dismiss();
                //todo add refresh

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: " + e.getMessage());
            }
        });

    }


    class ViewHolder extends RecyclerView.ViewHolder{

        public View viewBackground;
        public View viewForeground;
        @NonNull
        Dialog myDialog;
        RelativeLayout card_item;
        ImageView categoryImageView,cardBackground;
        TextView cardTitle , cardTargetDate;
        ImageView backgroundiconAchieved;
        int id;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            categoryImageView = itemView.findViewById(R.id.cardCategoryIcon);
            cardTitle = itemView.findViewById(R.id.cardTitle);
            cardTargetDate = itemView.findViewById(R.id.cardTargetDate);
            card_item = itemView.findViewById(R.id.card_item);
            cardBackground = itemView.findViewById(R.id.cardBackground);
            backgroundiconAchieved = itemView.findViewById(R.id.achieved_icon);

            //swipe
            viewBackground = itemView.findViewById(R.id.delete_background);
            viewForeground = itemView.findViewById(R.id.card_item);

            //inflating
            myDialog = new Dialog(context,android.R.style.Theme_Translucent_NoTitleBar);
            myDialog.setContentView(R.layout.popup_show_window);

        }
    }
}
