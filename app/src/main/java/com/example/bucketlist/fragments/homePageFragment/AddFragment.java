package com.example.bucketlist.fragments.homePageFragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.bucketlist.EditItem;
import com.example.bucketlist.R;
import com.example.bucketlist.data.DatabaseHandler;
import com.example.bucketlist.model.BucketItems;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.util.Calendar;

public class AddFragment extends Fragment implements DatePickerDialog.OnDateSetListener, View.OnClickListener {

    private static final String TAG = "AddFragment";
    TextView targetDateText;
    private boolean flag = false;
    private DatabaseHandler db;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private View view;


    Dialog myDialog;


    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mAuth = FirebaseAuth.getInstance();

        view = inflater.inflate(R.layout.fragment_add, container, false);
        CardView travelCategory = (CardView) view.findViewById(R.id.travelCategory);
        CardView adventureCategory = (CardView) view.findViewById(R.id.adventureCategory);
        CardView foodcategory = (CardView) view.findViewById(R.id.foodCategory);
        CardView relationCategory = (CardView) view.findViewById(R.id.relationCategory);
        CardView careerCategory = (CardView) view.findViewById(R.id.careerCategory);
        CardView financialCategory = (CardView) view.findViewById(R.id.financialCategory);
        CardView learningCategory = (CardView) view.findViewById(R.id.learningCategory);
        CardView healthCategory = (CardView) view.findViewById(R.id.healthCategory);
        CardView otherCategory = (CardView) view.findViewById(R.id.otherCategory);

        travelCategory.setOnClickListener(this);
        adventureCategory.setOnClickListener(this);
        foodcategory.setOnClickListener(this);
        relationCategory.setOnClickListener(this);
        careerCategory.setOnClickListener(this);
        financialCategory.setOnClickListener(this);
        learningCategory.setOnClickListener(this);
        healthCategory.setOnClickListener(this);
        otherCategory.setOnClickListener(this);
        db = new DatabaseHandler(getContext());
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
         if (mAuth.getCurrentUser() != null) {
             mUser = mAuth.getCurrentUser();
         }
    }

    @Override
    public void onClick(View v) {
        final AlertDialog.Builder alert;
        alert = new AlertDialog.Builder(getActivity());
        View view1 = getLayoutInflater().inflate(R.layout.popup_add_window,null);
        final TextView addPopupTitle = (TextView)view1.findViewById(R.id.addPopupTitle);

        switch (v.getId()){
            case R.id.travelCategory :
                addPopupTitle.setText("Travel");
                break;
            case R.id.adventureCategory :
                addPopupTitle.setText("Adventure");
                break;
            case R.id.foodCategory :
                addPopupTitle.setText("Food");
                break;
            case R.id.relationCategory :
                addPopupTitle.setText("Relation");
                break;
            case R.id.careerCategory :
                addPopupTitle.setText("Career");
                break;
            case R.id.financialCategory :
                addPopupTitle.setText("Financial");
                break;
            case R.id.learningCategory :
                addPopupTitle.setText("Learning");
                break;
            case R.id.healthCategory :
                addPopupTitle.setText("Health");
                break;
            case R.id.otherCategory :
                addPopupTitle.setText("Other");
                break;
        }


        final EditText nameText = view1.findViewById(R.id.nameText);
        final EditText describeText = view1.findViewById(R.id.descriptionText);
        final RelativeLayout targetDateLayout = view1.findViewById(R.id.targetdateLayout);
        targetDateText = view1.findViewById(R.id.targetDateText);
        final RadioButton publicButton = view1.findViewById(R.id.publicButton);
        final RadioButton privateButton = view1.findViewById(R.id.privateButton);
        final Button createButton = view1.findViewById(R.id.createButton);
        final ImageView cancelButton = view1.findViewById(R.id.cancelButton);

        alert.setView(view1);

        final AlertDialog addItemAlertDialog = alert.create();
        addItemAlertDialog.setCanceledOnTouchOutside(false);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItemAlertDialog.dismiss();

            }
        });

        targetDateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();

            }
        });

        privateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag = true;
            }
        });

        publicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag  =false;
            }
        });

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = nameText.getText().toString();
                String description = describeText.getText().toString();

                if(name.isEmpty()&& description.isEmpty())
                    Toast.makeText(getContext(), "Field is still empty", Toast.LENGTH_SHORT).show();
                else {
                    final BucketItems item =  new BucketItems();
                    item.setTitle(nameText.getText().toString().trim());
                    item.setCategory(addPopupTitle.getText().toString().trim());
                    item.setDeadline(targetDateText.getText().toString().trim());
                    item.setPrivate(flag);
                    item.setAchieved(false);
                    item.setInfo(description);
                    db.addItem(item);


                    try {
                        FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
                        DocumentReference documentReference = fireStore.collection("Users").document(mUser.getUid())
                                .collection("items").document();

                        item.setDateItemAdded(Long.toString(System.currentTimeMillis()));
                        item.setStringID(documentReference.getId());
                        documentReference.set(item).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "onSuccess: added successfully");
                                addItemAlertDialog.dismiss();


                                myDialog = new Dialog(getContext(),android.R.style.Theme_Translucent_NoTitleBar);
                                myDialog.setContentView(R.layout.popup_show_window);

                                final TextView titleOfCard = myDialog.findViewById(R.id.cardTitle);
                                final TextView infoOfCard = myDialog.findViewById(R.id.cardDescription);
                                final TextView targetOfCard = myDialog.findViewById(R.id.card_target_date);
                                final Button completedButton = myDialog.findViewById(R.id.completeButton);
                                ImageView editButton = myDialog.findViewById(R.id.editButton);
                                final ImageView privacyImageView = myDialog.findViewById(R.id.privacyImageView);
                                final ImageView card_background = myDialog.findViewById(R.id.card_background);
                                final TextView categoryTextView = myDialog.findViewById(R.id.categoryTextView);
                                final ImageView categoryImageView = myDialog.findViewById(R.id.categoryImageView);
                                final TextView privacyTextView = myDialog.findViewById(R.id.privacyTextView);

                                myDialog.show();


                                titleOfCard.setText(item.getTitle());
                                if (item.getInfo() != null) {
                                    infoOfCard.setText(item.getInfo());
                                }
                                completedButton.setText(item.isAchieved() ? "RE ACTIVATE": "ACCOMPLISHED");
                                privacyImageView.setImageResource(item.isPrivate()? R.drawable.ic_baseline_person_24: R.drawable.ic_baseline_public_24);
                                privacyTextView.setText(item.isPrivate()? "Private" : "Public");

                                targetOfCard.setText(item.getDeadline());
                                categoryTextView.setText(item.getCategory());
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


                                ImageView cancelButton2 = myDialog.findViewById(R.id.cancelButton2);
                                cancelButton2.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        myDialog.dismiss();
                                    }
                                });

                                editButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {


                                        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext(),R.style.BottomSheetDialogTheme);
                                        new EditItem(getContext(), item, mUser, bottomSheetDialog) {
                                            @Override
                                            protected void onEditComplete() {
                                               myDialog.dismiss();
                                                refreshMyDialog();
                                               myDialog.show();
                                            }
                                        };

                                    }

                                    private void refreshMyDialog() {
                                        titleOfCard.setText(item.getTitle());
                                        if (item.getInfo() != null) {
                                            infoOfCard.setText(item.getInfo());
                                        }
                                        completedButton.setText(item.isAchieved() ? "RE ACTIVATE": "ACCOMPLISHED");
                                        privacyImageView.setImageResource(item.isPrivate()? R.drawable.ic_baseline_person_24: R.drawable.ic_baseline_public_24);
                                        privacyTextView.setText(item.isPrivate()? "Private" : "Public");

                                        targetOfCard.setText(item.getDeadline());

                                        categoryTextView.setText(item.getCategory());
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
                                    }
                                });
                                myDialog.show();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                e.printStackTrace();
                                Toast.makeText(getContext(), "Failed to add : " + e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

            }
        });

        addItemAlertDialog.show();

    }


    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }


    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
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
        targetDateText.setText(date);
        targetDateText.setTextColor(getResources().getColor(R.color.blackcolor));
    }




}
