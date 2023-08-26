package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.example.myapplication.fragment.SplashScreenFragment;
import com.example.myapplication.utils.LoadingDialog;

public class MainActivity extends AppCompatActivity {
    private LoadingDialog loadingDialog;
    public static String BASE_URL = "http://10.24.54.87:9999/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        // Đăng ký vào chủ đề ("new_story") để nhận thông báo
//        FirebaseApp.initializeApp(this);
//        FirebaseMessaging.getInstance().subscribeToTopic("new_story")
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        // Đăng ký thành công
//                    } else {
//                        // Đăng ký thất bại
//                    }
//                });

        loadingDialog = new LoadingDialog(this);
        loadingDialog.create();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.layout_container, new SplashScreenFragment()).commit();
    }

    public static void switchToFragment(FragmentManager fragmentManager, Fragment fragment) {
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
    }


    @Override
    protected void onStart() {
        super.onStart();
    }
}