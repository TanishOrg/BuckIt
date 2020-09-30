package com.example.bucketlist.adapters;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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

import java.util.Calendar;
import java.util.List;


public class RecyclerAdapterDream extends ItemAdapter<RecyclerAdapterDream.ViewHolder> implements DatePickerDialog.OnDateSetListener {
//        extends RecyclerView.Adapter<RecyclerAdapterDream.ViewHolder>{

    private static final String TAG = "RECYCLERadapter";
    private Context context;
    private List<BucketItems> itemsList;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private BucketItemModify itemModify;

    Dialog categoryDialog;
    String name,discription,targetDate,category;
    ImageView cancelEditButton;
    ImageView doneEditButton;

    EditText nameEditText;
    EditText discriptionEditText;
    EditText targetEditText;

    RadioGroup radioGroup ;
    String stringPrivacy;
    RadioButton publicEditButton;
    RadioButton privateEditButton;

    RadioGroup categoryRadioGroup;
    String afterEditCategory ;
    boolean afterEditPrivacy ;
    RadioButton travelCard;
    RadioButton foodCard;
    RadioButton adventureCard;
    RadioButton careerCard ;
    RadioButton financialCard;
    RadioButton learningCard;
    RadioButton healthCard;
    RadioButton otherCard;
    RadioButton relationCard;
    boolean privacy;
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

    @Override
    protected void updateData(BucketItems items, final ViewHolder holder, final int position) {
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

    @Override
    protected void bindHolder(final ViewHolder holder, final BucketItems items, final int position) {

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
                FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
                final DocumentReference documentReference = fireStore.collection("Users").document(mUser.getUid())
                        .collection("items").document(items.getStringID());

                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context,R.style.BottomSheetDialogTheme);
                bottomSheetDialog.setContentView(R.layout.edit_item_bottom_sheet);

                 cancelEditButton = bottomSheetDialog.findViewById(R.id.cancelEditButton);
                 doneEditButton = bottomSheetDialog.findViewById(R.id.doneEditButton);

                nameEditText = bottomSheetDialog.findViewById(R.id.nameEditText);
                 discriptionEditText = bottomSheetDialog.findViewById(R.id.discriptionEditText);
                targetEditText = bottomSheetDialog.findViewById(R.id.targetEditText);

                radioGroup = bottomSheetDialog.findViewById(R.id.radioGroup);
                publicEditButton = bottomSheetDialog.findViewById(R.id.publicEditButton);
                privateEditButton = bottomSheetDialog.findViewById(R.id.privateEditButton);

                categoryRadioGroup = bottomSheetDialog.findViewById(R.id.categoryRadioGroup);
                travelCard = bottomSheetDialog.findViewById(R.id.travelCard);
                foodCard = bottomSheetDialog.findViewById(R.id.foodCard);
                adventureCard = bottomSheetDialog.findViewById(R.id.adventureCard);
                careerCard = bottomSheetDialog.findViewById(R.id.careerCard);
                financialCard = bottomSheetDialog.findViewById(R.id.financialCard);
                learningCard = bottomSheetDialog.findViewById(R.id.learningCard);
                healthCard = bottomSheetDialog.findViewById(R.id.healthCard);
                otherCard = bottomSheetDialog.findViewById(R.id.otherCard);
                relationCard = bottomSheetDialog.findViewById(R.id.relationCard);

                 name = items.getTitle();
                nameEditText.setText(name);

                 discription = items.getInfo();
                discriptionEditText.setText(discription);

                 targetDate = items.getDeadline();
                targetEditText.setText(targetDate);
                privacy = items.isPrivate();
                if (!privacy){
                    publicEditButton.setChecked(true);
                    stringPrivacy = publicEditButton.getText().toString();
                }
                else{
                    privateEditButton.setChecked(true);
                    stringPrivacy = privateEditButton.getText().toString();
                }


                 category = items.getCategory();
                switch (category){
                    case "Travel":
                        travelCard.setChecked(true);
                        break;
                    case "Adventure":
                        adventureCard.setChecked(true);
                        break;
                    case "Food":
                        foodCard.setChecked(true);
                        break;
                    case "Career":
                        careerCard.setChecked(true);
                        break;
                    case "Financial":
                        financialCard.setChecked(true);
                        break;
                    case "Health":
                        healthCard.setChecked(true);
                        break;
                    case "Relation":
                        relationCard.setChecked(true);
                        break;
                    case "Learning":
                        learningCard.setChecked(true);
                        break;
                    case "Other":
                        otherCard.setChecked(true);
                        break;
                }


                cancelEditButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.dismiss();
                        Toast.makeText(context, "Change is not updated", Toast.LENGTH_SHORT).show();
                    }
                });

                targetEditText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDatePickerDialog();
                    }
                });

                nameEditText.addTextChangedListener(EditActivityWatcher);


                publicEditButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) { afterEditPrivacy = false; }
                });

                privateEditButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) { afterEditPrivacy = true; }
                });



                travelCard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) { afterEditCategory = travelCard.getText().toString();
                    }
                });
                adventureCard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) { afterEditCategory = adventureCard.getText().toString();
                    }
                });
                foodCard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) { afterEditCategory = foodCard.getText().toString();
                    }
                });
                careerCard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) { afterEditCategory = careerCard.getText().toString();
                    }
                });
                relationCard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) { afterEditCategory = relationCard.getText().toString();
                    }
                });
                financialCard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) { afterEditCategory = financialCard.getText().toString();
                    }
                });
                learningCard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) { afterEditCategory = learningCard.getText().toString();
                    }
                });
                healthCard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) { afterEditCategory = healthCard.getText().toString();
                    }
                });
                otherCard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) { afterEditCategory = otherCard.getText().toString();
                    }
                });

                categoryRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        RadioButton radioButton = bottomSheetDialog.findViewById(checkedId);
                        if(!category.equals(radioButton.getText().toString()))
                        doneEditButton.setVisibility(View.VISIBLE);

                        else
                            doneEditButton.setVisibility(View.INVISIBLE);
                    }
                });

                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        RadioButton radioButton = bottomSheetDialog.findViewById(checkedId);
                        if(!stringPrivacy.equals(radioButton.getText().toString()) )
                            doneEditButton.setVisibility(View.VISIBLE);

                        else
                            doneEditButton.setVisibility(View.INVISIBLE);
                    }
                });

                doneEditButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (!name.toLowerCase().equals(nameEditText.getText().toString().toLowerCase())){
                            documentReference.update("title",nameEditText.getText().toString());
                            items.setTitle(nameEditText.getText().toString());

                        }

                        if(!discription.toLowerCase().equals(discriptionEditText.getText().toString().toLowerCase())){
                            documentReference.update("info",discriptionEditText.getText().toString());
                            items.setInfo(discriptionEditText.getText().toString());

                        }
                        if(!targetDate.equals(targetEditText.getText().toString())){
                            documentReference.update("deadline",targetEditText.getText().toString());
                            items.setDeadline(targetEditText.getText().toString());

                        }

                        if(!category.equals(afterEditCategory)){
                            documentReference.update("category",afterEditCategory);
                            items.setCategory(afterEditCategory);

                        }

                        if (privacy != afterEditPrivacy){
                            documentReference.update("private",afterEditPrivacy);
                            items.setPrivate(afterEditPrivacy);

                        }
                        notifyDataSetChanged();
                        bottomSheetDialog.dismiss();
                        Toast.makeText(context, "Activity Updated", Toast.LENGTH_SHORT).show();
                    }
                });

                bottomSheetDialog.show();
                bottomSheetDialog.setCanceledOnTouchOutside(false);


            }
        });




    }

    private TextWatcher EditActivityWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            if (!name.toLowerCase().equals(nameEditText.getText().toString().toLowerCase()) ||
                    !discription.toLowerCase().equals(discriptionEditText.getText().toString().toLowerCase()) ||
                    !targetDate.equals(targetEditText.getText().toString())){
                doneEditButton.setVisibility(View.VISIBLE);
            }
            else
                doneEditButton.setVisibility(View.INVISIBLE);

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

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

    private void deleteFromFireBase(final int position, final ViewHolder viewHolder, final OnItemDelete onItemDelete) {
        final BucketItems item = itemsList.get(position);
        FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
        DocumentReference documentReference = fireStore.collection("Users").document(mUser.getUid())
                .collection("items").document(item.getStringID());
        documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Snackbar.make(viewHolder.itemView,"Sucess",300).show();
                itemsList.remove(position);
                onItemDelete.refreshFragment();
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Snackbar.make(viewHolder.itemView,e.toString(),300).show();
            }
        });
    }




    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(context,this ,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        String monthName;
        switch (month){
            case 0:
                monthName ="Jan";
                break;
            case 1:
                monthName ="Feb";
                break;
            case 2:
                monthName ="Mar";
                break;
            case 3:
                monthName ="Apr";
                break;
            case 4:
                monthName ="May";
                break;
            case 5:
                monthName ="June";
                break;
            case 6:
                monthName ="July";
                break;
            case 7:
                monthName ="Aug";
                break;
            case 8:
                monthName ="Sept";
                break;
            case 9:
                monthName ="Oct";
                break;
            case 10:
                monthName ="Nov";
                break;
            case 11:
                monthName ="Dec";
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + month);
        }
        String date = monthName + " " + dayOfMonth + ", " + year;
        targetEditText.setText(date);
    }


}
