package com.example.myapplication.api_service;

import com.example.myapplication.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface AuthService {
    @POST("users/login")
    Call<User> login(@Body User user);

    @POST("users/register")
    Call<User> register(@Body User user);

    @GET("users/{id}")
    Call<User> getUserById(@Path("id") String id);

    @PATCH("users/{id}")
    Call<User> updateUser(@Path("id") String id, @Body User user);
}
