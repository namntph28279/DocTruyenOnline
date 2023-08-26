package com.example.myapplication.model;

import java.io.Serializable;

public class User implements Serializable {
    private String _id;
    private String email;
    private String fullName;
    private String password;
    private String username;
    private String createdAt;

    private String avatar;

    public User() {
    }

    public User(String fullName,String username)
    {
        this.fullName = fullName;
        this.username = username;
    }
    public User(String email, String password,int id ){
        this.email = email;
        this.password = password;
    }
    public User(String password)
    {
        this.password =password;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String get_id() {
        return _id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return "User{" +
                "_id='" + _id + '\'' +
                ", email='" + email + '\'' +
                ", fullName='" + fullName + '\'' +
                ", password='" + password + '\'' +
                ", username='" + username + '\'' +
                ", createdAt='" + createdAt + '\'' +

                ", avatar='" + avatar + '\'' +
                '}';
    }
}
