package com.example.myapplication.utils;


import android.content.Context;
import android.content.SharedPreferences;

public class AppPreferences {
    private static final String PREFS_NAME = "MyFile";
    private static final String KEY_ID_USER = "ID_USER";
    private static final String KEY_IS_FIRST_LOGIN = "isFirstLaunch";

    private static SharedPreferences prefs;
    private static SharedPreferences.Editor editor;

    public static boolean isFirstLogin() {
        return prefs.getBoolean(KEY_IS_FIRST_LOGIN, true);
    }

    public static void setFirstLogin(boolean value) {
        editor.putBoolean(KEY_IS_FIRST_LOGIN, value);
        editor.apply();
    }

    public AppPreferences(Context context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public static void saveKeyIdUser(Context context, String id) {
        prefs = context.getSharedPreferences(PREFS_NAME, context.MODE_PRIVATE);
        editor = prefs.edit();
        editor.putString(KEY_ID_USER, id);
        editor.apply();
    }

    public static String getKeyIdUser(Context context) {
        prefs = context.getSharedPreferences(PREFS_NAME, context.MODE_PRIVATE);
        return prefs.getString(KEY_ID_USER, "");
    }
}