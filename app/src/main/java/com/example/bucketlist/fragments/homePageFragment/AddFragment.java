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
import android.view.Window;
import android.view.WindowManager;
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
import com.example.bucketlist.PopUpShowItem;
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
import com.vansuita.gaussianblur.GaussianBlur;

import java.util.Calendar;

public class AddFragment extends Fragment implements DatePickerDialog.OnDateSetListener, View.OnClickListener {

    private static final String TAG = "AddFragment";
    TextView targetDateText;
    private boolean flag = false;
    private DatabaseHandler db;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseFirestore mFireStore;
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
//                Log.d("Button", "onClick: " + flag);
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
//                                getParentFragmentManager().
//                                        beginTransaction().replace(R.id.fragment_container,new ProfileFragment()).commit();
//                                Toast.makeText(getContext(), "Added to your bucket list", Toast.LENGTH_SHORT).show();
                                addItemAlertDialog.dismiss();
                                final Dialog myDialog = new Dialog(getContext(),android.R.style.Theme_Translucent_NoTitleBar);
                                new PopUpShowItem(getContext(),item,mUser,myDialog,true){

                                    @Override
                                    protected void onCompleteButtonClicked() {
                                        item.setAchieved(item.isAchieved() ? false:true);
                                        FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
                                        DocumentReference documentReference = fireStore.collection("Users").document(mUser.getUid())
                                                .collection("items").document(item.getStringID());
                                        documentReference.update(item.toHashMap()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d(TAG, "onSuccess: Sucess" );
                                                myDialog.dismiss();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.d(TAG, "onFailure: " + e.getMessage());
                                            }
                                        });
                                    }

                                    @Override
                                    protected void onEditButtonClick() {
                                        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext(),R.style.BottomSheetDialogTheme);
                                        new EditItem(getContext(),item,mUser,bottomSheetDialog){

                                            @Override
                                            protected void onEditComplete() {
                                              myDialog.dismiss();

                                            }
                                        };
                                    }
                                };

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
//                Log.d("Database", "onClick: " + db.getItemsCount() );
//                Log.d("Database", "onClick: " + db.getAllItems().toString());
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
