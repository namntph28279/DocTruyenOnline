package com.example.myapplication.model;

import java.util.List;

public class Comic {
    private String _id;
    private String author;
    private String avatar,desc;
    private List<String> list_content;
    private String name;
    private int year;

    public Comic(String _id, String author, String avatar, String desc, List<String> list_content, String name, int year) {
        this._id = _id;
        this.author = author;
        this.avatar = avatar;
        this.desc = desc;
        this.list_content = list_content;
        this.name = name;
        this.year = year;
    }

    public String get_id() {
        return _id;
    }

    public String getAuthor() {
        return author;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getDesc() {
        return desc;
    }

    public List<String> getList_content() {
        return list_content;
    }

    public String getName() {
        return name;
    }

    public int getYear() {
        return year;
    }

    @Override
    public String toString() {
        return "Comics{" +
                "_id='" + _id + '\'' +
                ", author='" + author + '\'' +
                ", avatar='" + avatar + '\'' +
                ", desc='" + desc + '\'' +
                ", list_content=" + list_content +
                ", name='" + name + '\'' +
                ", year=" + year +
                '}';
    }
}
