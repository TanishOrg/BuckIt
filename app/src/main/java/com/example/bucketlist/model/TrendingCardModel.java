package com.example.bucketlist.model;

import android.widget.ImageView;
import android.widget.TextView;

public class TrendingCardModel {

    String city,country;
    String backgroundImageUrl;


    public TrendingCardModel(String city, String country, String backgroundImageUrl) {
        this.city = city;
        this.country = country;
        this.backgroundImageUrl = backgroundImageUrl;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getBackgroundImageUrl() {
        return backgroundImageUrl;
    }

    public void setBackgroundImageUrl(String backgroundImage) {
        this.backgroundImageUrl = backgroundImage;
    }
}
