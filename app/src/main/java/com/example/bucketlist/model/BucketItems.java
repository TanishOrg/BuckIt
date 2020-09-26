package com.example.bucketlist.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import com.example.bucketlist.constants.Constants;

import java.util.Map;
import java.util.Objects;


public class BucketItems {

    private int id;
    private String category;
    private String info;
    private String title;
    private boolean isPrivate;
    private boolean achieved;
    private String dateItemAdded;
    private String deadline;

    public BucketItems() {}

    public BucketItems(int id, String category, String title, boolean isPrivate, boolean achieved, String dateItemAdded, String deadline) {
        this.id = id;
        this.category = category;
        this.title = title;
        this.isPrivate = isPrivate;
        this.achieved = achieved;
        this.dateItemAdded = dateItemAdded;
        this.deadline = deadline;
    }

    public BucketItems(int id, String category, String info, String title, boolean isPrivate, boolean achieved, String dateItemAdded, String deadline) {
        this.id = id;
        this.category = category;
        this.info = info;
        this.title = title;
        this.isPrivate = isPrivate;
        this.achieved = achieved;
        this.dateItemAdded = dateItemAdded;
        this.deadline = deadline;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public boolean isAchieved() {
        return achieved;
    }

    public void setAchieved(boolean achieved) {
        this.achieved = achieved;
    }

    public String getDateItemAdded() {
        return dateItemAdded;
    }

    public void setDateItemAdded(String dateItemAdded) {
        this.dateItemAdded = dateItemAdded;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }


    @Override
    public String toString() {
        return "BucketItems{" +
                "id=" + id +
                ", category='" + category + '\'' +
                ", info='" + info + '\'' +
                ", title='" + title + '\'' +
                ", isPrivate=" + isPrivate +
                ", achieved=" + achieved +
                ", dateItemAdded='" + dateItemAdded + '\'' +
                ", deadline='" + deadline + '\'' +
                '}';
    }

    public static  BucketItems hashToObject(Map map) {
        BucketItems item =new BucketItems();
        item.setAchieved((Boolean) map.get("achieved"));
        item.setDateItemAdded((String) map.get("dateItemAdded"));
        item.setCategory((String) map.get("category"));
        item.setTitle((String) map.get("title"));
        item.setInfo((String) map.get("info"));
        item.setPrivate((Boolean) map.get("private"));
        item.setDeadline((String) map.get("deadline"));
        item.setDateItemAdded((String) map.get("dateItemAdded"));
        return item;
    }
}
