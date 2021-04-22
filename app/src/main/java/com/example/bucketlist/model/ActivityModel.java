package com.example.bucketlist.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityModel {


    private String createdByUserID;
    private String title,postId;
    private String description;
    private long timeStamp;
    private String location;
    private int likes;
    private int dislikes;
    private List<String> category;
    private int totalComments;

    public ActivityModel(String createdByUserID, String title, long timeStamp, String location, int likes,int dislikes,String postId,int totalComments) {
        this.createdByUserID = createdByUserID;
        this.title = title;
        this.timeStamp = timeStamp;
        this.location = location;
        this.likes = likes;
        this.dislikes = dislikes;
        this.postId=postId;
        this.totalComments = totalComments;

    }

    public int getTotalComments() {
        return totalComments;
    }

    public void setTotalComments(int totalComments) {
        this.totalComments = totalComments;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
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
        this.timeStamp =timeStamp ;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
//        String[] arr = location.split(",");
//        String cityName = arr[0].trim() + ", " + arr[arr.length - 1].trim();
        this.location = location;
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

}
