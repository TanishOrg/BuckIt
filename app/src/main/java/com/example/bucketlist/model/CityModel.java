package com.example.bucketlist.model;

public class CityModel {
        private String image;
    private String city;
    private String country;

    public CityModel(String image, String city, String country) {
        this.image = image;
        this.city = city;
        this.country = country;
    }
//    public CityModel(int image, String city, String country) {
//        this.image = image;
//        this.city = city;
//        this.country = country;
//    }




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


