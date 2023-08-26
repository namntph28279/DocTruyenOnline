package com.example.myapplication.api_service;

import com.example.myapplication.model.Comic;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ComicService {

    @GET("comics/")
    Call<List<Comic>> getAllComics();

    @GET("comics/{id}")
    Call<Comic> getComics(@Path("id")String id);

    @GET("/comics/search/{q}")
    Call<List<Comic>> searchComics(@Path("q") String query);


}
