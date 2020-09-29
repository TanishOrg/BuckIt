package com.example.bucketlist.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bucketlist.HomeActivity;
import com.example.bucketlist.R;
import com.example.bucketlist.fragments.homePageFragment.ProfileFragment;
import com.example.bucketlist.layout.userLayout.EditProfileActivity;
import com.example.bucketlist.model.BucketItemModify;
import com.example.bucketlist.model.BucketItems;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;


public class RecyclerAdapterDream extends RecyclerView.Adapter<RecyclerAdapterDream.ViewHolder>{

    private static final String TAG = "RECYCLERadapter";
    private Context context;
    private List<BucketItems> itemsList;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private BucketItemModify itemModify;
    Dialog categoryDialog;

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
        String category = items.getCategory();
        switch (category){
            case "Travel":
                holder.categoryImageView.setImageResource(R.drawable.ic_baseline_flight_24);
                holder.cardBackground.setImageResource(R.mipmap.travelbackground);
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

    class ViewHolder extends RecyclerView.ViewHolder{


        @NonNull
        Dialog myDialog;
        RelativeLayout card_item;
        ImageView categoryImageView,cardBackground;
        TextView cardTitle , cardTargetDate;
        int id;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            categoryImageView = itemView.findViewById(R.id.cardCategoryIcon);
            cardTitle = itemView.findViewById(R.id.cardTitle);
            cardTargetDate = itemView.findViewById(R.id.cardTargetDate);
            card_item = itemView.findViewById(R.id.card_item);
            cardBackground = itemView.findViewById(R.id.cardBackground);

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
        TextView categoryTextView = holder.myDialog.findViewById(R.id.categoryTextView);
        ImageView categoryImageView = holder.myDialog.findViewById(R.id.categoryImageView);
        TextView privacyTextView = holder.myDialog.findViewById(R.id.privacyTextView);
        ImageView cancelButton2 = holder.myDialog.findViewById(R.id.cancelButton2);
        ImageView editButton = holder.myDialog.findViewById(R.id.editButton);
        ImageView card_background = holder.myDialog.findViewById(R.id.card_background);
        final LinearLayout categoryLayout = holder.myDialog.findViewById(R.id.categoryLayout);

        titleOfCard.setText(items.getTitle());
        if (items.getInfo() != null) {
            infoOfCard.setText(items.getInfo());
        }
        completedButton.setText(items.isAchieved() ? "RE ACTIVATE": "ACCOMPLISHED");

        privacyImageView.setImageResource(items.isPrivate()? R.drawable.ic_baseline_person_24: R.drawable.ic_baseline_public_24);
        privacyTextView.setText(items.isPrivate()? "Private" : "Public");

        targetOfCard.setText(items.getDeadline());

        categoryTextView.setText(items.getCategory());
        switch (categoryTextView.getText().toString()){
            case "Travel":
                categoryImageView.setImageResource(R.drawable.ic_baseline_flight_24);
                card_background.setImageResource(R.mipmap.travelbackground);
                break;
            case "Adventure":
                categoryImageView.setImageResource(R.drawable.ic_backpack);
                card_background.setImageResource(R.mipmap.adventurebackground);
                break;
            case "Food":
                categoryImageView.setImageResource(R.drawable.ic_hamburger);
                card_background.setImageResource(R.mipmap.foodbackground);
                break;
            case "Relation":
                categoryImageView.setImageResource(R.drawable.ic_heart);
                card_background.setImageResource(R.mipmap.relationbackground);
                break;
            case "Career":
                categoryImageView.setImageResource(R.drawable.ic_portfolio);
                card_background.setImageResource(R.mipmap.careerbackground);
                break;
            case "Financial":
                categoryImageView.setImageResource(R.drawable.ic_financial);
                card_background.setImageResource(R.mipmap.financialbackground);
                break;
            case "Learning":
                categoryImageView.setImageResource(R.drawable.ic_reading_book);
                card_background.setImageResource(R.mipmap.learningbackground);
                break;
            case "Health":
                categoryImageView.setImageResource(R.drawable.ic_health);
                card_background.setImageResource(R.mipmap.healthbackground);
                break;
            case "Other":
                categoryImageView.setImageResource(R.drawable.ic_menu);
                break;

        }


        holder.card_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(context, "test click" + String.valueOf(holder.getAdapterPosition()), Toast.LENGTH_SHORT).show();
                holder.myDialog.show();
            }
        });


        cancelButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.myDialog.dismiss();
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

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                categoryLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        categoryDialog = new Dialog(context,android.R.style.Theme_Translucent_NoTitleBar);
                        categoryDialog.setContentView(R.layout.category_horizontal_slide_floating);
                        categoryDialog.show();
//                         CardView travelCard = categoryDialog.findViewById(R.id.travelCard);
//                         CardView otherCard = categoryDialog.findViewById(R.id.otherCard);
//                         CardView foodCard = categoryDialog.findViewById(R.id.foodCard);
//                         CardView adventureCard = categoryDialog.findViewById(R.id.adventureCard);
//                         CardView careerCard = categoryDialog.findViewById(R.id.careerCard);
//                         CardView relationCard = categoryDialog.findViewById(R.id.relationCard);
//                         CardView healthCard = categoryDialog.findViewById(R.id.healthCard);
//                         CardView financialCard = categoryDialog.findViewById(R.id.financialCard);


                    }
                });

            }
        });


    }


}
