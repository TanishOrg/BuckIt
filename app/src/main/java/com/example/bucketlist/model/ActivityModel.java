package com.example.bucketlist.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityModel {


    private String createdByUserID;
    private String title;
    private String description;
    private long timeStamp;
    private String location;
    private int likes;
    private int dislikes;
    private List<String> category;
    public ActivityModel(){

    }

    public String getCreatedByUserID() {
        return createdByUserID;
    }

    public void setCreatedByUserID(String createdByUserID) {
        this.createdByUserID = createdByUserID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public List<String> getCategory() {
        return category;
    }

    public void setCategory(List<String> category) {
        this.category = category;
    }

    public void setTimeStamp() {
        this.timeStamp = System.currentTimeMillis();
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        String[] arr = location.split(",");
        String cityName = arr[0].trim() + ", " + arr[arr.length - 1].trim();
        this.location = cityName;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getDislikes() {
        return dislikes;
    }

    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
    }

    public Map toHashMap() {
        Map map = new HashMap();
        map.put("title",title.trim());
        map.put("description",description);
        map.put("likes",likes);
        map.put("dislikes",dislikes);
        map.put("timeStamp",timeStamp);
        map.put("createdBy",createdByUserID);
        map.put("location",location);
        map.put("category",category);
        return map;
    }

}
