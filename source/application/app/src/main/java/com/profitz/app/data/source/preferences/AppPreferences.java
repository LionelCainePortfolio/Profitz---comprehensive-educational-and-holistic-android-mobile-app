package com.profitz.app.data.source.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.profitz.app.data.Constants;

public class AppPreferences implements Preferences {

    private static final String PREF_KEY_CURRENT_DISPLAYED_PROMOS = "current_displayed_promos";

    private static volatile AppPreferences INSTANCE;
    private final SharedPreferences mPrefs;

    private AppPreferences(Context context) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static AppPreferences getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new AppPreferences(context);
        }
        return INSTANCE;
    }

    @Override
    public String getCurrentDisplayedPromos() {
        return mPrefs.getString(PREF_KEY_CURRENT_DISPLAYED_PROMOS, Constants.PROMOS_POPULAR);
    }

    @Override
    public void setCurrentDisplayedPromos(String currentDisplayedPromos) {
        mPrefs.edit().putString(PREF_KEY_CURRENT_DISPLAYED_PROMOS, currentDisplayedPromos).apply();
    }
}
