package com.dhananjay.exposyslabs;
public class DataContent {
    private int photoId;
    private String title,desc;

    public DataContent(){}
    public DataContent(int photoId, String title, String desc) {
        this.photoId = photoId;
        this.title = title;
        this.desc = desc;
    }

    public int getPhotoId() {
        return photoId;
    }

    public void setPhotoId(int photoId) {
        this.photoId = photoId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
