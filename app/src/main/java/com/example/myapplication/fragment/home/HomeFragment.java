package com.example.myapplication.fragment.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.adapter.ComicsAdapter;
import com.example.myapplication.api_service.ComicService;
import com.example.myapplication.model.Comic;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment {
    private GridView listView;
    private List<Comic> tempList;

    private ComicsAdapter adapter;
    private final String TAG = "HOMEFRAGMENT";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews(view);
        tempList = new ArrayList<>();
        adapter = new ComicsAdapter(getContext(), tempList);
        listView.setAdapter(adapter);
        fetchApiFromServer();
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchApiFromServer();
    }

    private void fetchApiFromServer() {
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
                if (response.isSuccessful()) {
                    tempList.clear();
                    tempList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Comic>> call, Throwable t) {

            }
        });
    }

    private void findViews(View view) {
        listView = view.findViewById(R.id.listView);
    }


}