package com.esdrasmorais.inspetoronline.data;

import android.content.Context;
import android.content.SharedPreferences;

public class SecurityPreferences {
    private SharedPreferences sharedPreferences;

    public SecurityPreferences(Context context) {
        this.sharedPreferences = context.getSharedPreferences(
            "SharedData",
            Context.MODE_PRIVATE
        );
    }

    public void storeString(String key, String value) {
        this.sharedPreferences.edit().putString(key, value).apply();
    }

    public String getStoredString(String key) {
        return this.sharedPreferences.getString(key, "");
    }
}
