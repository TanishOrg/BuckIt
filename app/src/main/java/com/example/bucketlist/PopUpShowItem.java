package com.example.bucketlist;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bucketlist.model.BucketItems;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.vansuita.gaussianblur.GaussianBlur;

import kotlin.OptionalExpectation;

public abstract class PopUpShowItem implements View.OnClickListener {
    public Dialog myDialog;
    private FirebaseFirestore firebaseFirestore;
    private Context context;
    private FirebaseUser mUser;
    BucketItems item;
    TextView titleOfCard;
    TextView infoOfCard;
    TextView targetOfCard;
    Button completedButton;
    ImageView editButton;
    ImageView privacyImageView;
    ImageView card_background;
    TextView categoryTextView;
    ImageView categoryImageView;
    TextView privacyTextView;
    ImageView cancelButton2;
    private InterstitialAd mInterstitialAd;
   public PopUpShowItem(Context context , BucketItems item ,FirebaseUser user, Dialog myDialog,Boolean isDialogShow){

       firebaseFirestore = FirebaseFirestore.getInstance();
       this.context = context;
       this.mUser = user;
       this.myDialog = myDialog;
       this.item = item;
        initializeUi();

       mInterstitialAd = new InterstitialAd(context);
       mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
       mInterstitialAd.loadAd(new AdRequest.Builder().build());
        if (isDialogShow) myDialog.show();
        myDialog.setCanceledOnTouchOutside(false);
   }

   private void initializeUi(){
       myDialog.setContentView(R.layout.popup_show_window);
        titleOfCard = myDialog.findViewById(R.id.cardTitle);
       infoOfCard = myDialog.findViewById(R.id.cardDescription);
        targetOfCard = myDialog.findViewById(R.id.card_target_date);
        completedButton = myDialog.findViewById(R.id.completeButton);
       editButton = myDialog.findViewById(R.id.editButton);
        privacyImageView = myDialog.findViewById(R.id.privacyImageView);
       card_background = myDialog.findViewById(R.id.card_background);
       categoryTextView = myDialog.findViewById(R.id.categoryTextView);
        categoryImageView = myDialog.findViewById(R.id.categoryImageView);
        privacyTextView = myDialog.findViewById(R.id.privacyTextView);
       cancelButton2 = myDialog.findViewById(R.id.cancelButton2);

        setVar();
   }

   public void setVar(){
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
               GaussianBlur.with(context).radius(6).put(R.mipmap.travelbackground,card_background);
               break;
           case "Adventure":
               categoryImageView.setImageResource(R.drawable.ic_backpack);
               card_background.setImageResource(R.mipmap.adventurebackground);
               GaussianBlur.with(context).radius(6).put(R.mipmap.adventurebackground,card_background);
               break;
           case "Food":
               categoryImageView.setImageResource(R.drawable.ic_hamburger);
               card_background.setImageResource(R.mipmap.foodbackground);
               GaussianBlur.with(context).radius(6).put(R.mipmap.foodbackground,card_background);
               break;
           case "Relation":
               categoryImageView.setImageResource(R.drawable.ic_heart);
               card_background.setImageResource(R.mipmap.relationbackground);
               GaussianBlur.with(context).radius(6).put(R.mipmap.relationbackground,card_background);
               break;
           case "Career":
               categoryImageView.setImageResource(R.drawable.ic_portfolio);
               card_background.setImageResource(R.mipmap.careerbackground);
               GaussianBlur.with(context).radius(6).put(R.mipmap.careerbackground,card_background);
               break;
           case "Financial":
               categoryImageView.setImageResource(R.drawable.ic_financial);
               card_background.setImageResource(R.mipmap.financialbackground);
               GaussianBlur.with(context).radius(6).put(R.mipmap.financialbackground,card_background);
               break;
           case "Learning":
               categoryImageView.setImageResource(R.drawable.ic_reading_book);
               card_background.setImageResource(R.mipmap.learningbackground);
               GaussianBlur.with(context).radius(6).put(R.mipmap.learningbackground,card_background);
               break;
           case "Health":
               categoryImageView.setImageResource(R.drawable.ic_health);
               card_background.setImageResource(R.mipmap.healthbackground);
               GaussianBlur.with(context).radius(6).put(R.mipmap.healthbackground,card_background);
               break;
           case "Other":
               categoryImageView.setImageResource(R.drawable.ic_menu);
               card_background.setImageResource(R.mipmap.otherbackground);
               GaussianBlur.with(context).radius(6).put(R.mipmap.otherbackground,card_background);
               break;

       }

       setOnclick();


   }

    private void setOnclick() {
       cancelButton2.setOnClickListener(this);
       editButton.setOnClickListener(this);
       completedButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

       switch (v.getId()){
           case R.id.cancelButton2:
               if (mInterstitialAd.isLoaded()) {
                   mInterstitialAd.show();
               } else {
                   Log.d("TAG", "The interstitial wasn't loaded yet.");
               }
               myDialog.dismiss();

               break;
           case R.id.completeButton:
               onCompleteButtonClicked();
               break;
           case R.id.editButton:
                onEditButtonClick();
               break;
       }

    }

    public void showDialog() {
       myDialog.show();
    }

    public void hideEditButton() {
        editButton.setVisibility(View.INVISIBLE);
    }

    protected abstract void onCompleteButtonClicked();

    protected abstract void onEditButtonClick();
}
