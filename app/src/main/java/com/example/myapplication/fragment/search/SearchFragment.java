package com.example.myapplication.fragment.search;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.adapter.ComicsAdapter;
import com.example.myapplication.api_service.ComicService;
import com.example.myapplication.model.Comic;
import com.example.myapplication.utils.LoadingDialog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class SearchFragment extends Fragment {


    private androidx.appcompat.widget.SearchView searchView;
    private GridView listView;
    private TextView tvError;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews(view);
        fetchDataFromApi();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterComics(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void filterComics(String query)
    {
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ComicService comicService = retrofit.create(ComicService.class);
        LoadingDialog.dialogLoading.show();
        Call<List<Comic>> call = comicService.searchComics(query);

        call.enqueue(new Callback<List<Comic>>() {
            @Override
            public void onResponse(Call<List<Comic>> call, Response<List<Comic>> response) {
                List<Comic> comic = response.body();
                if (response.isSuccessful()){
                    ComicsAdapter adapter = new ComicsAdapter(getContext(),comic);
                    listView.setAdapter(adapter);
                }else{
                    tvError.setVisibility(View.VISIBLE);
                    tvError.setText("KHÔNG TÌM THẤY");
                    new Handler().postDelayed(() -> {
                        tvError.setVisibility(View.GONE);
                    }, 2000);
                }

            }

            @Override
            public void onFailure(Call<List<Comic>> call, Throwable t) {

            }
        });
        LoadingDialog.dialogLoading.dismiss();

    }
    private void fetchDataFromApi()
    {
        LoadingDialog.dialogLoading.show();
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ComicService comicService = retrofit.create(ComicService.class);

        Call<List<Comic>> call = comicService.getAllComics();

        call.enqueue(new Callback<List<Comic>>() {
            @Override
            public void onResponse(Call<List<Comic>> call, Response<List<Comic>> response) {
                if (response.isSuccessful()){
                    List<Comic> comic = response.body();
                    Collections.shuffle(comic);

                    ComicsAdapter adapter = new ComicsAdapter(getContext(),comic);
                    listView.setAdapter(adapter);
                }

            }

            @Override
            public void onFailure(Call<List<Comic>> call, Throwable t) {

            }
        });
        LoadingDialog.dialogLoading.dismiss();  }

    private void findViews(View view)
    {
        searchView = view.findViewById(R.id.searchView);
        listView = view.findViewById(R.id.listView);
        tvError = view.findViewById(R.id.tvError);
        tvError.setVisibility(View.GONE);
    }
}