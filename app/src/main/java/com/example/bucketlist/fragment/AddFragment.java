package com.example.bucketlist.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.bucketlist.R;
import com.example.bucketlist.data.DatabaseHandler;
import com.example.bucketlist.model.BucketItems;

import java.util.Calendar;

public class AddFragment extends Fragment implements DatePickerDialog.OnDateSetListener, View.OnClickListener {

    TextView targetDateText;
    private boolean flag = false;
    private DatabaseHandler db;
    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add, container, false);

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


        final EditText nameText = (EditText)view1.findViewById(R.id.nameText);
        final EditText describeText = (EditText)view1.findViewById(R.id.nameText);
        final RelativeLayout targetDateLayout = (RelativeLayout)view1.findViewById(R.id.targetdateLayout);
        targetDateText = (TextView)view1.findViewById(R.id.targetDateText);
        final RadioButton publicButton = (RadioButton)view1.findViewById(R.id.publicButton);
        final RadioButton privateButton = (RadioButton)view1.findViewById(R.id.privateButton);
        final Button createButton = (Button)view1.findViewById(R.id.createButton);
        final ImageView cancelButton = (ImageView)view1.findViewById(R.id.cancelButton);

        alert.setView(view1);

        final AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(false);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();

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
                Log.d("Button", "onClick: " + flag);
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
                    BucketItems item =  new BucketItems();
                    item.setTitle(nameText.getText().toString().trim());
                    item.setCategory(addPopupTitle.getText().toString().trim());
                    item.setDeadline(targetDateText.getText().toString().trim());
                    item.setPrivate(flag);
                    item.setAchieved(true);
                    db.addItem(item);
                    Log.d("flag", "onClick: " + flag);
                    Log.d("Database", "onClick: " + db.getItemsCount() );
                    Log.d("Database", "onClick: " + db.getAllItems().toString());
                    Toast.makeText(getContext(), "Added to your bucket list", Toast.LENGTH_SHORT).show();
                }
                Log.d("Database", "onClick: " + db.getItemsCount() );
                Log.d("Database", "onClick: " + db.getAllItems().toString());


            }
        });

        alertDialog.show();

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
        String monthName ;
        switch (month){
            case 0:
                monthName = "January";
                break;
            case 1:
                monthName = "February";
                break;
            case 2:
                monthName = "March";
                break;
            case 3:
                monthName = "April";
                break;
            case 4:
                monthName = "May";
                break;
            case 5:
                monthName = "June";
                break;
            case 6:
                monthName = "July";
                break;
            case 7:
                monthName = "August";
                break;
            case 8:
                monthName = "September";
                break;
            case 9:
                monthName = "October";
                break;
            case 10:
                monthName = "November";
                break;
            case 11:
                monthName = "December";
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + month);
        }
        String date = monthName + " " + dayOfMonth + ", " + year;
        targetDateText.setText(date);
        targetDateText.setTextColor(getResources().getColor(R.color.blackcolor));

    }


}
