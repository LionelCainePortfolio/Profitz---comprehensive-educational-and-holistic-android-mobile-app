package com.profitz.app.promos;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;

import com.profitz.app.promos.activities.LoginActivity;
import com.profitz.app.util.Utility;
import com.google.gson.Gson;

public class MyPreferenceManager {
    private static final String SHARED_PREF_NAME = "pref_name";
    private static final String KEY_USERNAME = "user_name";
    private static final String KEY_NAME = "name";
    private static final String KEY_LASTNAME = "lastname";
    private static final String KEY_EMAIL = "user_email";
    private static final String KEY_ID = "user_id";
    private static final String KEY_FAVOURITE_COUNT = "favourite_count";
    private static final String KEY_DONE_COUNT = "done_count";
    private static final String KEY_POINTS = "points";
    private static final String KEY_EARNED = "earned";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";
    private static final String KEY_POWER_LEVEL = "power_level";
    private static final String KEY_PREMIUM = "premium";
    private static final String KEY_IS_BAN = "is_ban";
    private static final String KEY_IS_ONLINE = "is_online";
    private static final String KEY_UNREAD_MESSAGES = "unread_messages";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_FCM_TOKEN = "fcm_token";
    private static final String KEY_LAST_SCENE = "last_scene";
    private static final String KEY_PICTURE = "picture";
    private static final String KEY_COUNTRY= "country";
    private static final String KEY_LAST_MSG = "last_msg";
    private static final String KEY_LAST_MSG_DATE = "last_msg_date";
    private static final String KEY_LAST_MSG_SENT_BY_ME = "last_msg_sent_by_me";

    private static MyPreferenceManager mInstance;
    private static Context mCtx;
    private static final String IS_LOGIN = "IS_LOGIN";
    private static final String LOGIN_USERNAME = "LOGIN_USERNAME";
    private static final String SERVER_ROOT = "SERVER_ROOT";

    private static final String LOGIN_USERMODEL = "LOGIN_USERMODEL";
    private static final String IS_FIRST_TIME = "IS_FIRST_TIME_v1_4";
    private SharedPreferences pref;


    public MyPreferenceManager(Context context) {
        mCtx = context;
    }

    public static synchronized MyPreferenceManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new MyPreferenceManager(context);
        }
        return mInstance;
    }
    SharedPreferences.Editor putDouble(final SharedPreferences.Editor edit, final String key, final double value) {
        return edit.putLong(key, Double.doubleToRawLongBits(value));
    }

    double getDouble(final SharedPreferences prefs, final String key, final double defaultValue) {
        return Double.longBitsToDouble(prefs.getLong(key, Double.doubleToLongBits(defaultValue)));
    }
    public void setUserLogin(User user) {

        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_ID, Integer.parseInt(user.getId()));
        editor.putString(KEY_NAME, user.getName());
        editor.putString(KEY_LASTNAME, user.getLastname());
        editor.putString(KEY_USERNAME, user.getUsername());
        editor.putString(KEY_EMAIL, user.getEmail());
        editor.putInt(KEY_FAVOURITE_COUNT, user.getFavourite_count());
        editor.putInt(KEY_DONE_COUNT, user.getDone_count());
        putDouble(editor, KEY_POINTS, user.getPoints());
        editor.putString(KEY_PHONE, user.getPhone());
        editor.putString(KEY_PICTURE, user.getPicture());
        editor.putString(KEY_COUNTRY, user.getCountry());
        putDouble(editor, KEY_EARNED, user.getEarned());
        editor.putBoolean(KEY_IS_LOGGED_IN,true);
        editor.apply();
        String jsonObject = new Gson().toJson(user);
//        pref.edit().putString(LOGIN_USERMODEL, jsonObject).apply();
    }
    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    //this method will give the logged in user
    public User getUser() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        return new User(
                sharedPreferences.getInt(KEY_ID, -1),
                sharedPreferences.getString(KEY_NAME, null),
                sharedPreferences.getString(KEY_LASTNAME, null),
                sharedPreferences.getString(KEY_USERNAME, null),
                sharedPreferences.getString(KEY_EMAIL, null),
                sharedPreferences.getInt(KEY_FAVOURITE_COUNT, 0),
                sharedPreferences.getInt(KEY_DONE_COUNT, 0),
                getDouble(sharedPreferences, KEY_POINTS, 0),
                getDouble(sharedPreferences, KEY_EARNED, 0),
                sharedPreferences.getInt(KEY_POWER_LEVEL, 0),
                sharedPreferences.getInt(KEY_PREMIUM, 0),
                sharedPreferences.getBoolean(KEY_IS_BAN, false),
                sharedPreferences.getBoolean(KEY_IS_ONLINE, true),
                sharedPreferences.getInt(KEY_UNREAD_MESSAGES, 0),
                sharedPreferences.getString(KEY_PHONE, null),
                sharedPreferences.getString(KEY_COUNTRY, null),
                sharedPreferences.getString(KEY_FCM_TOKEN, null),
                sharedPreferences.getString(KEY_LAST_SCENE, null),
                sharedPreferences.getString(KEY_PICTURE, null),
                sharedPreferences.getString(KEY_LAST_MSG, null),
                sharedPreferences.getString(KEY_LAST_MSG_DATE, null),
                sharedPreferences.getString(KEY_LAST_MSG_SENT_BY_ME, null)
        );
    }
    public void logout() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        Intent intent1 = new Intent(new Intent(mCtx, LoginActivity.class));
        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mCtx.startActivity(intent1);
    }

    public void removeLoginUser() {
        pref.edit().remove(LOGIN_USERMODEL).apply();
    }
    public void setLogin(boolean isLogin) {
        if (!isLogin) {
            pref.edit().remove(LOGIN_USERNAME).apply();
        }
        pref.edit().putBoolean(IS_LOGIN, isLogin).apply();
    }
    public boolean isLogin() {
        return pref.getBoolean(IS_LOGIN, false);
    }
    public String getServerRoot() {
        return pref.getString(SERVER_ROOT, Utility.API_ROOT_URL);
    }

    public void setServerRoot(String serverRoot) {
        pref.edit().putString(SERVER_ROOT, serverRoot).apply();
    }

    public void setFirstTime(boolean isFirstTime) {
        pref.edit().putBoolean(IS_FIRST_TIME, isFirstTime).apply();
    }

    public boolean isFirstTime() {
        return pref.getBoolean(IS_FIRST_TIME, true);
    }
}
