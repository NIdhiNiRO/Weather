package com.virtusa.weather.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.virtusa.weather.BuildConfig;

/**
 * Created by nidhiparekh on 10/11/17.
 */
public class PreferenceUtil {

    public static final String PREF_NAME = "SearchPref";
    public static final String SEARCHED_ITEM = "searchedItem";
    private static PreferenceUtil mInstance;
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;
    //store the app level
    public PreferenceUtil(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, 0);
        editor = sharedPreferences.edit();
    }

    public static void PrintLog(String message) {
        if (BuildConfig.DEBUG) {
            Log.d("System ", message);
        }
    }

    public static PreferenceUtil getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new PreferenceUtil(context);
        }
        return mInstance;
    }

    public static String getLastSearchText() {
        return sharedPreferences.getString(SEARCHED_ITEM, "");
    }

    public static void setLastSearchText(String userId) {
        editor.putString(SEARCHED_ITEM, userId);
        editor.commit();
    }

    public void clearData() {
        editor.clear();
        editor.commit();
    }

}
