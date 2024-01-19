package com.example.labproject.Admin.faculty;

public class TeacherData
{
    private String name, phone, email, post,image,key;
    //private String name, phone, email, post,image,key;

    public TeacherData() {
    }

    public TeacherData(String name, String phone, String email, String post, String image, String key) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.post = post;
        this.image = image;
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
