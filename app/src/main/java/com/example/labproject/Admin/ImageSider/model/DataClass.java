package com.example.labproject.Admin.ImageSider.model;

public class DataClass {

    private String imageURL;
    private String  caption;

    private String key;

    public DataClass() {


    }

    public DataClass(String imageURL, String caption, String key) {
        this.imageURL = imageURL;
        this.caption = caption;
        this.key = key;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
