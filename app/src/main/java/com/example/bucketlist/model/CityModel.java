package com.example.bucketlist.model;

public class CityModel {
    private int image;
    private String city;
    private String country;

//    public CityModel(int image, String city, String country) {
//        this.image = image;
//        this.city = city;
//        this.country = country;
//    }

    public CityModel() {
        this.image = image;
    }


    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
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
}


