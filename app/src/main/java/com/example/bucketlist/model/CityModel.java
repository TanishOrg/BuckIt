package com.example.bucketlist.model;

public class CityModel {
    private String image;
    private String city;
    private String country;
    private String stringId;

    public CityModel(String image, String city, String country,String stringId) {
        this.image = image;
        this.city = city;
        this.country = country;
        this.stringId = stringId;
    }

//    public CityModel(int image, String city, String country) {
//        this.image = image;
//        this.city = city;
//        this.country = country;
//    }


    public String getStringId() {
        return stringId;
    }

    public void setStringId(String stringId) {
        this.stringId = stringId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
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


