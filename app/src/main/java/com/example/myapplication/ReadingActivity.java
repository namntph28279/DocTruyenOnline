package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.example.myapplication.adapter.ImageListAdapter;

import java.util.List;

public class ReadingActivity extends AppCompatActivity {
    private List<String> imageUrls;

    private ListView listView;
    private final String TAG="READING";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading);
        initView();

        listView = findViewById(R.id.listView);

        Intent intent = getIntent();
        imageUrls = intent.getStringArrayListExtra("list_content");
        Log.e(TAG, "onCreate: "+imageUrls );
        // Create and set the custom adapter
        ImageListAdapter adapter = new ImageListAdapter(this, imageUrls);
        listView.setAdapter(adapter);

    }

    private void initView() {
        listView = findViewById (R.id.listView);
    }
}