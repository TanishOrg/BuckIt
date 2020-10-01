package com.example.bucketlist;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bucketlist.model.BucketItems;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditItem
        implements View.OnClickListener , RadioGroup.OnCheckedChangeListener {
    private FirebaseFirestore firebaseFirestore;
    private BottomSheetDialog bottomSheetDialog;
    private ImageView cancelEditButton;
    private ImageView doneEditButton;

    private EditText nameEditText;
    private EditText discriptionEditText;
    private EditText targetEditText;

    private RadioGroup radioGroup ;
    private String stringPrivacy;
    private RadioButton publicEditButton;
    private RadioButton privateEditButton;

    private RadioGroup categoryRadioGroup;
    private String afterEditCategory ;
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

    //variables
    private String name;
    private String description;
    private String targetDate;
    private String category;

    //Bucket Item
    private BucketItems item;



    EditItem(Context context, BucketItems item) {
        firebaseFirestore = FirebaseFirestore.getInstance();
        bottomSheetDialog = new BottomSheetDialog(context,R.style.BottomSheetDialogTheme);
        this.item = item;
        initializeUi();
        setVar();
        setOnclick();
    }

   private void initializeUi() {
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
   }

   private void setVar() {
       name = item.getTitle();
       nameEditText.setText(name);

       description = item.getInfo();
       discriptionEditText.setText(description);

       targetDate = item.getDeadline();
       targetEditText.setText(targetDate);
       privacy = item.isPrivate();

       if (!privacy){
           publicEditButton.setChecked(true);
           stringPrivacy = publicEditButton.getText().toString();
       }
       else{
           privateEditButton.setChecked(true);
           stringPrivacy = privateEditButton.getText().toString();
       }

       setCategory();
   }

   private void setCategory() {
       category = item.getCategory();
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
   }

    private void setOnclick() {
        cancelEditButton.setOnClickListener(this);
        targetEditText.setOnClickListener(this);
        nameEditText.setOnClickListener(this);
        publicEditButton.setOnClickListener(this);
        privateEditButton.setOnClickListener(this);
        travelCard.setOnClickListener(this);
        doneEditButton.setOnClickListener(this);
        discriptionEditText.setOnClickListener(this);
        travelCard.setOnClickListener(this);
        foodCard.setOnClickListener(this);
        adventureCard.setOnClickListener(this);
        careerCard.setOnClickListener(this);
        financialCard.setOnClickListener(this);
        healthCard.setOnClickListener(this);
        relationCard.setOnClickListener(this);
        learningCard.setOnClickListener(this);
        otherCard.setOnClickListener(this);
        categoryRadioGroup.setOnCheckedChangeListener(this);
        radioGroup.setOnCheckedChangeListener(this);

    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {

    }
}
