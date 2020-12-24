package com.example.bucketlist.adapters;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bucketlist.EditItem;
import com.example.bucketlist.PopUpShowItem;
import com.example.bucketlist.R;
import com.example.bucketlist.fragments.profileFragments.DreamFragment;
import com.example.bucketlist.layout.userLayout.DetailProfile;
import com.example.bucketlist.model.BucketItemModify;
import com.example.bucketlist.model.BucketItems;
import com.example.bucketlist.model.ItemAdapter;
import com.example.bucketlist.model.OnItemDelete;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;


public class RecyclerAdapterDream extends ItemAdapter<RecyclerAdapterDream.ViewHolder> implements View.OnClickListener {
//        extends RecyclerView.Adapter<RecyclerAdapterDream.ViewHolder>{

    private static final String TAG = "RECYCLERadapter";
    private ImageView clearButton;

    private Context context;
    private List itemsList;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private BucketItemModify itemModify;

    private TextView deleteMultiItem;
    DreamFragment dreamFragment;

    private static final int CONTENT = 0;
    private static final int AD = 1;

    private List<BucketItems> bucketItemsListToRemove = new ArrayList<>();

        public RecyclerAdapterDream(Context context, List<BucketItems> items, BucketItemModify modify,
                                    TextView deleteMultiItem, ImageView clearButton , DreamFragment dreamFragment) {
        this.context = context;
        this.itemsList = items;
        this.itemModify = modify;
        this.deleteMultiItem = deleteMultiItem;
        this.dreamFragment = dreamFragment;
        this.clearButton = clearButton;
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
        View view;
        if (viewType == AD){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.banner_ad_container, parent, false);
            return new AdviewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_dream, parent, false);
            return new ContentViewHolder(view);
        }
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int position) {

        if (viewHolder instanceof ContentViewHolder){
            ContentViewHolder holder = (ContentViewHolder) viewHolder;
            final BucketItems items =(BucketItems) itemsList.get(position);

            holder.id = items.getId();
            holder.cardTitle.setText(items.getTitle());
            holder.cardTargetDate.setText(items.getDeadline());
            String category = items.getCategory();
            if (category != null) {
                switch (category) {
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
                        holder.cardBackground.setImageResource(R.mipmap.otherbackground);
                        break;

                }
            }

            bindHolder(holder,items,position);

            if (dreamFragment.fposition == position){
                holder.checkBox.setChecked(true);
                dreamFragment.fposition = -1;
            }

            if (dreamFragment.isActionMode){
                holder.checkBox.setVisibility(View.VISIBLE);
            }

            else {
                holder.checkBox.setVisibility(View.GONE);
                holder.checkBox.setChecked(false);
            }

            deleteMultiItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(context,R.style.AlertDialog);
                    dialog.setTitle("Are You Sure");
                    dialog.setMessage("This items after deletion cannot be retrieved");
                    dialog.setCancelable(true);
                    dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialogInterface) {
                        }
                    })
                            .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    deleteMultipleFromFireBase(dreamFragment.bucketItemsListToRemove);
                                }
                            })
                            .setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });
                    dialog.create();
                    dialog.show();

                }
            });

            clearButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dreamFragment.isActionMode = false;
                    dreamFragment.counterTextView.setText("0 item selected");
                    dreamFragment.floatingOptionLayout.setVisibility(View.GONE);
                    dreamFragment.bucketItemsListToRemove.clear();
                    dreamFragment.count = 0;
                    notifyDataSetChanged();
                }
            });

        } else if(viewHolder instanceof AdviewHolder) {
            AdView adView =(AdView) itemsList.get(position);
            AdviewHolder adviewHolder = (AdviewHolder) viewHolder;

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
    public int getItemCount() {
        return itemsList != null ? itemsList.size() : 0;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public int getItemViewType(int position) {
            if (itemsList.get(position) instanceof AdView){
                return AD;
            }
            else return CONTENT;
    }

    public abstract class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(@NonNull View itemView) {

            super(itemView);

        }
    }

    private class AdviewHolder extends  ViewHolder{
        public AdviewHolder(@NonNull final View itemView) {
            super(itemView);
        }
    }

    public class ContentViewHolder extends ViewHolder{

        @NonNull
        Dialog myDialog;
        RelativeLayout card_item;
        ImageView categoryImageView,cardBackground;
        TextView cardTitle , cardTargetDate;

        CheckBox checkBox;
        public View viewBackground;
        public View viewForeground;

        int id;
        public ContentViewHolder(@NonNull View itemView) {
            super(itemView);

            categoryImageView = itemView.findViewById(R.id.cardCategoryIcon);
            cardTitle = itemView.findViewById(R.id.cardTitle);
            cardTargetDate = itemView.findViewById(R.id.cardTargetDate);
            card_item = itemView.findViewById(R.id.card_item);
            cardBackground = itemView.findViewById(R.id.cardBackground);
            cardBackground.setClipToOutline(true);
            //swipe
            viewBackground = itemView.findViewById(R.id.delete_background);
            viewForeground = itemView.findViewById(R.id.card_item);

            checkBox = itemView.findViewById(R.id.checkBox);


            //inflating
            myDialog = new Dialog(context,android.R.style.Theme_Translucent_NoTitleBar);
            myDialog.setContentView(R.layout.popup_show_window);



        }
    }

    @Override
    protected void updateData(BucketItems items, final ViewHolder hold, final int position) {
        if (hold instanceof ContentViewHolder){
            final ContentViewHolder holder =(ContentViewHolder) hold;
            FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
            DocumentReference documentReference = fireStore.collection("Users").document(mUser.getUid())
                    .collection("items").document(items.getStringID());
            documentReference.update(items.toHashMap()).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d(TAG, "onSuccess: Sucess" );
                    holder.myDialog.dismiss();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "onFailure: " + e.getMessage());
                }
            });
        }
    }

    @Override
    protected void bindHolder(final ViewHolder hold, final BucketItems items, final int position) {

            if (hold instanceof  ContentViewHolder) {
                final ContentViewHolder holder = (ContentViewHolder) hold;

                new PopUpShowItem(context, items, mUser, holder.myDialog, false) {
                    @Override
                    protected void onCompleteButtonClicked() {
                        items.setAchieved(items.isAchieved() ? false : true);
                        updateData(items, holder, position);
                    }

                    @Override
                    protected void onEditButtonClick() {
                        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context, R.style.BottomSheetDialogTheme);
                        new EditItem(context, items, mUser, bottomSheetDialog) {
                            @Override
                            protected void onEditComplete() {
                                notifyDataSetChanged();
                            }
                        };
                    }
                };

                holder.card_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.myDialog.show();

                    }
                });

                holder.card_item.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        ;
                        dreamFragment.startSelection(position);
                        return true;
                    }
                });

                holder.checkBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dreamFragment.check(v, position);
                    }
                });
            }

    }


    @Override
    public void deleteItem(final int position, final ViewHolder holder, final OnItemDelete onItemDelete) {

        if (holder instanceof ContentViewHolder) {
            final ContentViewHolder viewHolder = (ContentViewHolder) holder;
            Log.d(TAG, "deleteItem: ");
            new AlertDialog.Builder(context,R.style.AlertDialog).setTitle("Are You Sure")
                    .setMessage("This item after deletion cannot be retrieved")
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
                    .setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Snackbar.make(viewHolder.itemView,"Cancelled",300).show();
                            notifyDataSetChanged();
                        }
                    })
                    .create()
                    .show();
        }

    }

    private void deleteMultipleFromFireBase(final List<BucketItems> bucketItemModifies) {
        FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
        CollectionReference reference = fireStore.collection("Users").document(mUser.getUid())
                .collection("items");
        for (BucketItems item : bucketItemModifies ) {
            Log.d(TAG, "deleteFromFireBase: " + item.getStringID());
            reference.document(item.getStringID())
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            e.printStackTrace();
                        }
                    });

        }
        bucketItemModifies.clear();
        dreamFragment.count = 0;
        dreamFragment.isActionMode = false;
        dreamFragment.counterTextView.setText("0 item selected");
        dreamFragment.floatingOptionLayout.setVisibility(View.INVISIBLE);
        notifyDataSetChanged();
    }

    @Override
    public void moveItem(int position, ViewHolder holder) {
        if (holder instanceof ContentViewHolder){
            ContentViewHolder viewHolder = (ContentViewHolder) holder;
            BucketItems items =(BucketItems) itemsList.get(position);
            items.setAchieved(items.isAchieved() ? false:true);
            updateData(items,viewHolder,position);
        }
    }

    private void deleteFromFireBase(final int position, final ContentViewHolder viewHolder, final OnItemDelete onItemDelete) {
        final BucketItems item =(BucketItems) itemsList.get(position);
        FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
        DocumentReference documentReference = fireStore.collection("Users").document(mUser.getUid())
                .collection("items").document(item.getStringID());
        documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Snackbar.make(viewHolder.itemView,e.toString(),300).show();
            }
        });
    }


}
