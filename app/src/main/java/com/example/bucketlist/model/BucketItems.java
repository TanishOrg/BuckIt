package com.example.bucketlist.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import com.example.bucketlist.constants.Constants;


public class BucketItems {

    private int id;
    private String category;
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
                ", title='" + title + '\'' +
                ", isPrivate=" + isPrivate +
                ", achieved=" + achieved +
                ", dateItemAdded='" + dateItemAdded + '\'' +
                ", deadline='" + deadline + '\'' +
                '}';
    }
}
