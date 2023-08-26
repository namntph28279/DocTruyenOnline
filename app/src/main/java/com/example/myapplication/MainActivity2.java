package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.adapter.CommentAdapter;
import com.example.myapplication.api_service.ComicService;
import com.example.myapplication.api_service.CommentService;
import com.example.myapplication.model.Comic;
import com.example.myapplication.model.Comment;
import com.example.myapplication.utils.AppPreferences;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity2 extends AppCompatActivity {
    private ImageView img_closeDialog;
    private Dialog dialog;
    private EditText edit_text;
    private AppCompatButton btn_sendComment;
    private TextView mTvNameComics;
    private TextView mTvDescComics;
    private CommentAdapter adapter;
    private TextView mTvAuthorComics;
    private TextView mTvYearComics;
    private ImageView img_avatarComics;

    private RecyclerView recyclerView;
    private Comic comic;

    private final String TAG = "MainActivity2";
    private Intent i;
    private AppCompatButton btn_readComics, btn_closeComics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.am2);
        initView();
        i = getIntent();
        setData(i.getStringExtra("comics_id"));
        getListComment(i.getStringExtra("comics_id"));
        adapter = new CommentAdapter(MainActivity2.this, new ArrayList<>());
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity2.this));
        recyclerView.setAdapter(adapter);
        btn_readComics.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity2.this, ReadingActivity.class);
            intent.putStringArrayListExtra("list_content", new ArrayList<>(comic.getList_content()));
            startActivity(intent);
        });

        btn_closeComics.setOnClickListener(view -> {
            showDialogComment(i.getStringExtra("comics_id"));
        });
    }

    private void showDialogComment(String comics_id) {
        String _idUser = AppPreferences.getKeyIdUser(MainActivity2.this);

        dialog = new Dialog(MainActivity2.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_write_comment);
        img_closeDialog = dialog.findViewById(R.id.img_closeDialog);
        edit_text = dialog.findViewById(R.id.edit_text);
        btn_sendComment = dialog.findViewById(R.id.btn_sendComment);

        img_closeDialog.setOnClickListener(v1 -> {
            dialog.dismiss();
        });
        btn_sendComment.setOnClickListener(view -> {
            String data = edit_text.getText().toString();
            writeComment(new Comment(data, comics_id, _idUser));
            finish();

        });
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    private void writeComment(Comment comment) {
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        CommentService commentService = retrofit.create(CommentService.class);

        Call<Comment> call = commentService.postAComment(comment);
        call.enqueue(new Callback<Comment>() {
            @Override
            public void onResponse(Call<Comment> call, Response<Comment> response) {
                Toast.makeText(MainActivity2.this, "Thành công", Toast.LENGTH_SHORT).show();
                getListComment(i.getStringExtra("comics_id")); // Fetch and update the comments immediately after posting

            }

            @Override
            public void onFailure(Call<Comment> call, Throwable t) {
                Log.e(TAG, "onFailure: WRITE COMMENT" + t);
            }
        });
    }

    private void getListComment(String id) {
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        CommentService commentService = retrofit.create(CommentService.class);

        Call<List<Comment>> call = commentService.getCommentComics(id);

        call.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                if (response.isSuccessful()) {
                    List<Comment> list = response.body();
                    Log.e(TAG, "onResponseM2: " + list.lastIndexOf(1));
                    Collections.sort(list, (a, b) -> b.getTime().compareTo(a.getTime()));

                    adapter.updateCommentData(list);

                    adapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                Log.e(TAG, "onFailure: GET LIST COMMENT " + t);
            }
        });
    }

    private void setData(String comics_id) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ComicService comicsInterface = retrofit.create(ComicService.class);
        Call<Comic> objCall = comicsInterface.getComics(comics_id);
        objCall.enqueue(new Callback<Comic>() {
            @Override
            public void onResponse(Call<Comic> call, Response<Comic> response) {
                if (response.isSuccessful()) {
                    comic = response.body();
                    mTvAuthorComics.setText("Tác giả: " + comic.getAuthor());
                    mTvNameComics.setText(comic.getName());
                    Picasso.get().load(comic.getAvatar()).into(img_avatarComics);
                    mTvDescComics.setText(comic.getDesc());
                    mTvYearComics.setText("Năm xuất bản: " + comic.getYear());
                }
            }

            @Override
            public void onFailure(Call<Comic> call, Throwable t) {
                //log errors
                Log.e(TAG, "onFailure: Can't get api " + t);

            }
        });
    }

    private void initView() {

        mTvNameComics = findViewById(R.id.tv_nameComics);
        mTvDescComics = findViewById(R.id.tv_descComics);
        mTvAuthorComics = findViewById(R.id.tv_authorComics);
        mTvYearComics = findViewById(R.id.tv_yearComics);
        img_avatarComics = findViewById(R.id.img_avatarComics);
        btn_readComics = findViewById(R.id.btn_readComics);
        btn_closeComics = findViewById(R.id.btn_closeComics);
        recyclerView = findViewById(R.id.recyclerViewComment);
    }


}