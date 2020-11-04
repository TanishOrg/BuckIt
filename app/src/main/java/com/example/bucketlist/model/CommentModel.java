package com.example.bucketlist.model;

public class CommentModel {
    String uid,comment;
    private long timeStamp;
    private int likes;

    public CommentModel(String uid, String comment, long timeStamp, int likes) {
        this.uid = uid;
        this.comment = comment;
        this.timeStamp = timeStamp;
        this.likes = likes;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }
}
