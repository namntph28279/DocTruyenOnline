package com.example.myapplication.api_service;

import com.example.myapplication.model.Comment;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CommentService {
    @GET("comments/{id}")
    Call<List<Comment>> getCommentComics(@Path("id") String id);


    @POST("comments/")
    Call<Comment> postAComment(@Body Comment comment);

    @PATCH("comments/{id}")
    Call<List<Comment>> updateComment(@Path("id") String id,@Body Comment comment);

    @DELETE("comments/{id}")
    Call<Void> deleteComment(@Path("id") String id);


}
