package com.example.homepage;

import android.content.Context;
import android.content.SharedPreferences;

public class SPUMaster {
    public static final String PREF_KEY_MODE = "mode";
    public static final String PREF_KEY_COUNT = "count";
    public static final String PREF_KEY_TIMESTAMP = "timestamp";
    private static final String PARENT_ID_KEY = "parent_id";
    private static final String PARENT_PASS = "parent_pass";
    private static final String CHILD_ID_KEY = "child_id";
    public static final String CHILD_PASS = "child_pass";

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
    }

    public static String getMode(Context context) {
        return getSharedPreferences(context).getString(PREF_KEY_MODE, "");
    }

    public static void saveMode(Context context, String mode) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(PREF_KEY_MODE, mode);
        editor.apply();
    }

    public static int getCount(Context context) {
        return getSharedPreferences(context).getInt(PREF_KEY_COUNT, 0);
    }

    public static void saveCount(Context context, int count) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putInt(PREF_KEY_COUNT, count);
        editor.apply();
    }

    public static long getTimestamp(Context context) {
        return getSharedPreferences(context).getLong(PREF_KEY_TIMESTAMP, 0L);
    }

    public static void saveTimestamp(Context context, long timestamp) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putLong(PREF_KEY_TIMESTAMP, timestamp);
        editor.apply();
    }

    public static String getParentId(Context context) {
        return getSharedPreferences(context).getString(PARENT_ID_KEY, "");
    }

    public static void saveParentId(Context context, String parentId) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(PARENT_ID_KEY, parentId);
        editor.apply();
    }

    public static String getParentPassword(Context context) {
        return getSharedPreferences(context).getString(PARENT_PASS, "");
    }

    public static void saveParentPassword(Context context, String password) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(PARENT_PASS, password);
        editor.apply();
    }

    public static String getChildId(Context context) {
        return getSharedPreferences(context).getString(CHILD_ID_KEY, "");
    }

    public static void saveChildId(Context context, String childId) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(CHILD_ID_KEY, childId);
        editor.apply();
    }

    public static String getChildPassword(Context context) {
        return getSharedPreferences(context).getString(CHILD_PASS, "");
    }

    public static void saveChildPassword(Context context, String password) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(CHILD_PASS, password);
        editor.apply();
    }
}

//
//// Example usage in an activity or any other context
//
//    // Get the mode value
//    String mode = SPUMaster.getMode(getApplicationContext());
//
//// Save a new mode value
//         SPUMaster.saveMode(getApplicationContext(), "parentmode");
//
//// Get the count value
//        int count = SPUMaster.getCount(getApplicationContext());
//
//// Save a new count value
//        SPUMaster.saveCount(getApplicationContext(), 10);
//
//// Get the timestamp value
//        long timestamp = SPUMaster.getTimestamp(getApplicationContext());
//
//// Save a new timestamp value
//        SPUMaster.saveTimestamp(getApplicationContext(), System.currentTimeMillis());
//
//// Get the parent ID value
//        String parentId = SPUMaster.getParentId(getApplicationContext());
//
//// Save a new parent ID value
//        SPUMaster.saveParentId(getApplicationContext(), "123456");
//
//// Get the parent password value
//        String parentPassword = SPUMaster.getParentPassword(getApplicationContext());
//
//// Save a new parent password value
//        SPUMaster.saveParentPassword(getApplicationContext(), "password123");
//
//// Get the child ID value
//        String childId = SPUMaster.getChildId(getApplicationContext());
//
//// Save a new child ID value
//        SPUMaster.saveChildId(getApplicationContext(), "987654");
//
//// Get the child password value
//        String childPassword = SPUMaster.getChildPassword(getApplicationContext());
//
//// Save a new child password value
//        SPUMaster.saveChildPassword(getApplicationContext(), "childpass");
