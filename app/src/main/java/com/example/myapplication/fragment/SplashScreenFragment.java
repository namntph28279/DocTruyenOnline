package com.example.myapplication.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;
import com.example.myapplication.fragment.auth.LoginFragment;

public class SplashScreenFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash_screen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        new CountDownTimer(3000, 1500) {
            @Override
            public void onTick(long l) {
            }

            @Override
            public void onFinish() {
//                if (AppPreferences.isFirstLogin()) {
//                } else {
//                    MainActivity.switchToFragment(getActivity().getSupportFragmentManager(), new MainFragment());

//                }
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.layout_container, new LoginFragment()).commit();
//

            }
        }.start();
    }
}