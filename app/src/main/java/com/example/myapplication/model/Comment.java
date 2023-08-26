package com.example.myapplication.model;

public class Comment {
    private String _id;
    private String data;
    private String id_comic;
    private User id_user;
    private String time;

    public Comment(){};

    public Comment(String _id, String data, String id_comic, User id_user, String time) {
        this._id = _id;
        this.data = data;
        this.id_comic = id_comic;
        this.id_user = id_user;
        this.time = time;
    }

    public Comment(String data, String id_comic, String userId) {
        this.data = data;
        this.id_comic = id_comic;
        this.id_user = new User();
        this.id_user.set_id(userId);
    }
    public Comment(String data)
    {
        this.data = data;
    }


    public User getId_user() {
        return id_user;
    }

    public void setId_user(User id_user) {
        this.id_user = id_user;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getId_comic() {
        return id_comic;
    }

    public void setId_comic(String id_comic) {
        this.id_comic = id_comic;
    }


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "_id='" + _id + '\'' +
                ", data='" + data + '\'' +
                ", id_comic='" + id_comic + '\'' +
                ", id_user='" + id_user + '\'' +
                ", time='" + time + '\'' +
                '}';
    }

}

