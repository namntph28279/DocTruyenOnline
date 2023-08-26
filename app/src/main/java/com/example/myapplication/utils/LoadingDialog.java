package com.example.myapplication.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.example.myapplication.R;

public class LoadingDialog extends Dialog {
    private final Context context;
    public static LoadingDialog dialogLoading;

    public LoadingDialog(@NonNull Context context) {
        super(context);
        this.context = context;
        setContentView(R.layout.dialog_loading);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setCancelable(false);

    }

    public void create() {
        dialogLoading = new LoadingDialog(context);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


}
