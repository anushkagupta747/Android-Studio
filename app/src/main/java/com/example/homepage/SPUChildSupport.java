package com.example.homepage;

import android.content.Context;
import android.content.SharedPreferences;

public class SPUChildSupport {
    public static final String PREF_KEY_TOKEN = "token";
    public static final String PREF_KEY_CHILDCOUNT = "child_count";

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
    }

    public static String getToken(Context context) {
        return getSharedPreferences(context).getString(PREF_KEY_TOKEN, "");
    }

    public static void saveToken(Context context, String token) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(PREF_KEY_TOKEN, token);
        editor.apply();
    }

    public static int getChildCount(Context context) {
        return getSharedPreferences(context).getInt(PREF_KEY_CHILDCOUNT, 0);
    }

    public static void saveChildCount(Context context, int childCount) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putInt(PREF_KEY_CHILDCOUNT, childCount);
        editor.apply();
    }
}
//
//    String token = SPUChildSupport.getToken(getApplicationContext());
//    SPUChildSupport.saveToken(getApplicationContext(), "your_token_value");
//        int childCount = SPUChildSupport.getChildCount(getApplicationContext());
//        SPUChildSupport.saveChildCount(getApplicationContext(), 5);