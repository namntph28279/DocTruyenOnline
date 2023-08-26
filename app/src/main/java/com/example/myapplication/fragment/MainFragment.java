package com.example.myapplication.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.fragment.home.HomeFragment;
import com.example.myapplication.fragment.profile.ProfileFragment;
import com.example.myapplication.fragment.search.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainFragment extends Fragment {
    private int item_selected = R.id.nav_Home;
    private BottomNavigationView bottomNav;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bottomNav = view.findViewById(R.id.bottomNav);
        bottomNav.getMenu().findItem(R.id.nav_Home).setChecked(true);
        if (savedInstanceState == null) {
            // Switch to the HomeFragment as the default fragment when the app starts
            MainActivity.switchToFragment(getActivity().getSupportFragmentManager(), new HomeFragment());
        }

        bottomNav.setOnItemSelectedListener(item -> {
            if (item.getItemId() == item_selected) return true;

            if (item.getItemId() == R.id.nav_Home) {
                MainActivity.switchToFragment(getActivity().getSupportFragmentManager(), new HomeFragment());
            } else if (item.getItemId() == R.id.nav_Search) {
                MainActivity.switchToFragment(getActivity().getSupportFragmentManager(), new SearchFragment());
            } else if (item.getItemId() == R.id.nav_User) {
                MainActivity.switchToFragment(getActivity().getSupportFragmentManager(), new ProfileFragment());
            }

            item_selected = item.getItemId();
            return true;
        });
    }


}