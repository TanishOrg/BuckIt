package com.example.bucketlist;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bucketlist.model.BucketItems;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;

public abstract class EditItem
        implements View.OnClickListener , RadioGroup.OnCheckedChangeListener , TextWatcher, DatePickerDialog.OnDateSetListener {
    private static final String TAG = "EditItem";
    private FirebaseFirestore firebaseFirestore;
    public BottomSheetDialog bottomSheetDialog;
    private Context context;
    private FirebaseUser mUser;

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



    public EditItem(Context context, BucketItems item, FirebaseUser user, BottomSheetDialog bottomSheetDialog) {
        firebaseFirestore = FirebaseFirestore.getInstance();
        this.context = context;
        this.mUser = user;
        this.bottomSheetDialog = bottomSheetDialog;
        this.item = item;
        initializeUi();

        nameEditText.addTextChangedListener(this);
        discriptionEditText.addTextChangedListener(this);
        targetEditText.addTextChangedListener(this);

        bottomSheetDialog.show();
        bottomSheetDialog.setCanceledOnTouchOutside(false);
    }

   private void initializeUi() {
       bottomSheetDialog.setContentView(R.layout.edit_item_bottom_sheet);
       cancelEditButton = bottomSheetDialog.findViewById(R.id.cancelEditButton);
       doneEditButton = bottomSheetDialog.findViewById(R.id.doneEditButton);

       nameEditText = bottomSheetDialog.findViewById(R.id.nameEditText);
//       Log.d(TAG, "initializeUi: " + nameEditText != null ? nameEditText.toString():"Error");
       discriptionEditText = bottomSheetDialog.findViewById(R.id.discriptionEditText);
       targetEditText = bottomSheetDialog.findViewById(R.id.targetEditText);

       radioGroup = bottomSheetDialog.findViewById(R.id.radioGroup);
       publicEditButton = bottomSheetDialog.findViewById(R.id.publicEditButton);
       privateEditButton = bottomSheetDialog.findViewById(R.id.privateEditButton);

       categoryRadioGroup = bottomSheetDialog.findViewById(R.id.categoryRadioGroup);

       Log.d(TAG, "initializeUi: " + item.toString());
       travelCard = bottomSheetDialog.findViewById(R.id.travelCard);
       foodCard = bottomSheetDialog.findViewById(R.id.foodCard);
       adventureCard = bottomSheetDialog.findViewById(R.id.adventureCard);
       careerCard = bottomSheetDialog.findViewById(R.id.careerCard);
       financialCard = bottomSheetDialog.findViewById(R.id.financialCard);
       learningCard = bottomSheetDialog.findViewById(R.id.learningCard);
       healthCard = bottomSheetDialog.findViewById(R.id.healthCard);
       otherCard = bottomSheetDialog.findViewById(R.id.otherCard);
       relationCard = bottomSheetDialog.findViewById(R.id.relationCard);
       setVar();
   }

   private void setVar() {
       name = item.getTitle();
       nameEditText.setText(name);

       description = item.getInfo();
       discriptionEditText.setText(description);
//       category = item.getCategory();
       afterEditPrivacy = item.isPrivate();

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

       setOnclick();
   }

   private void setCategory() {
       category = item.getCategory();
       int index = 0;
       switch (category){
           case "Travel":
               index = 0;
               travelCard.setChecked(true);
               break;
           case "Adventure":
               index = 1;
               adventureCard.setChecked(true);
               break;
           case "Food":
               index = 2;
               foodCard.setChecked(true);
               break;
           case "Career":
               index = 3;
               careerCard.setChecked(true);
               break;
           case "Financial":
               index = 4;
               financialCard.setChecked(true);
               break;
           case "Relation":
               index = 5;
               relationCard.setChecked(true);
               break;
           case "Learning":
               index = 6;
               learningCard.setChecked(true);
               break;
           case "Health":
               index = 7;
               healthCard.setChecked(true);
               break;
           case "Other":
               index = 8;
               otherCard.setChecked(true);
               break;
       }
//       ((RadioButton)radioGroup.getChildAt(index)).setChecked(true);
   }

    private void setOnclick() {
        cancelEditButton.setOnClickListener(this);
        targetEditText.setOnClickListener(this);
        nameEditText.setOnClickListener(this);
        publicEditButton.setOnClickListener(this);
        privateEditButton.setOnClickListener(this);
        doneEditButton.setOnClickListener(this);

        discriptionEditText.setOnClickListener(this);
        travelCard.setOnClickListener(this);
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

        switch (view.getId()) {
            case R.id.cancelEditButton :
                bottomSheetDialog.dismiss();
                break;
            case R.id.doneEditButton:
                final DocumentReference documentReference = firebaseFirestore.collection("Users").document(mUser.getUid())
                        .collection("items").document(item.getStringID());
                editField(documentReference);
                break;
            case R.id.targetEditText:
                showDatePickerDialog();
                break;
            case R.id.publicEditButton:
                afterEditPrivacy = false;
                break;
            case R.id.privateEditButton:
                afterEditPrivacy = true;
                break;
            case R.id.travelCard:
                afterEditCategory = travelCard.getText().toString();
                break;
            case R.id.foodCard:
                afterEditCategory = foodCard.getText().toString();
                break;
            case R.id.adventureCard:
                afterEditCategory = adventureCard.getText().toString();
                break;
            case R.id.careerCard:
                afterEditCategory = careerCard.getText().toString();
                break;
            case R.id.financialCard:
                afterEditCategory = financialCard.getText().toString();
                break;
            case R.id.healthCard:
                afterEditCategory = healthCard.getText().toString();
                break;
            case R.id.learningCard:
                afterEditCategory = learningCard.getText().toString();
                break;
            case R.id.relationCard:
                afterEditCategory = relationCard.getText().toString();
                break;
            case R.id.otherCard:
                afterEditCategory = otherCard.getText().toString();
                break;
        }

    }

    private void editField(DocumentReference documentReference) {
        if (!name.toLowerCase().equals(nameEditText.getText().toString().toLowerCase())){
            documentReference.update("title",nameEditText.getText().toString());
            item.setTitle(nameEditText.getText().toString());
        }
        if(!description.toLowerCase().equals(discriptionEditText.getText().toString().toLowerCase())){
            documentReference.update("info",discriptionEditText.getText().toString());
            item.setInfo(discriptionEditText.getText().toString());

        }
        if(!targetDate.equals(targetEditText.getText().toString())){
            documentReference.update("deadline",targetEditText.getText().toString());
            item.setDeadline(targetEditText.getText().toString());

        }

        if(!category.equals(afterEditCategory) && afterEditCategory != null){
            documentReference.update("category",afterEditCategory);
            item.setCategory(afterEditCategory);

        }

        if (privacy != afterEditPrivacy ){
            documentReference.update("private",afterEditPrivacy);
            item.setPrivate(afterEditPrivacy);

        }
//                                                notifyDataSetChanged();
//        myDialog.dismiss();
        bottomSheetDialog.dismiss();
        onEditComplete();
        Toast.makeText(context, "Activity Updated", Toast.LENGTH_SHORT).show();

    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        RadioButton radioButton;
        switch (radioGroup.getId()) {
            case R.id.categoryRadioGroup :
                radioButton = bottomSheetDialog.findViewById(checkedId);
                if(!category.equals(radioButton.getText().toString()))
                    doneEditButton.setVisibility(View.VISIBLE);
                else
                    doneEditButton.setVisibility(View.INVISIBLE);
                break;
            case R.id.radioGroup :
                radioButton = bottomSheetDialog.findViewById(checkedId);
                if(!stringPrivacy.equals(radioButton.getText().toString()) )
                    doneEditButton.setVisibility(View.VISIBLE);
                else
                    doneEditButton.setVisibility(View.INVISIBLE);
                break;
        }

    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        if (!name.toLowerCase().equals(nameEditText.getText().toString().toLowerCase()) ||
                !description.toLowerCase().equals(discriptionEditText.getText().toString().toLowerCase()) ||
                !targetDate.equals(targetEditText.getText().toString())){
            doneEditButton.setVisibility(View.VISIBLE);
        }
        else
            doneEditButton.setVisibility(View.INVISIBLE);

    }

    @Override
    public void afterTextChanged(Editable s) {

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
        targetEditText.setText(date);

    }

    protected abstract void onEditComplete();
}
