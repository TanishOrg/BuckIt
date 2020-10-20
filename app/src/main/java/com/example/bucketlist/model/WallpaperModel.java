package com.example.bucketlist.model;

public class WallpaperModel {
    private String photoId ;
    private String rawUrl;


    public WallpaperModel() {
    }


    public WallpaperModel(String photoId, String rawUrl) {
        this.photoId = photoId;
        this.rawUrl = rawUrl;

    }

    public String getPhotoId() {
        return photoId;
    }

    public void setPhotoId(String photoId) {
        this.photoId = photoId;
    }

    public String getRawUrl() {
        return rawUrl;
    }

    public void setRawUrl(String rawUrl) {
        this.rawUrl = rawUrl;
    }

}
