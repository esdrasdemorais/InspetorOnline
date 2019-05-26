package com.esdrasmorais.inspetoronline.data;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

public class SpTrans {
    private JsonObject json;
    private RequestQueue requestQueue;
    private String url;
    private Context context;

    public SpTrans(Context context, Location location) {
        this.context = context;
        this.url = "http://api.olhovivo.sptrans.com.br/v2.1";
        //this.authenticate();
    }

    public String getApiToken() {
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA
            );
            if (appInfo.metaData != null) {
                return appInfo.metaData.getString("br.com.sptrans.api.TOKEN");
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("SpTrans", e.getMessage());
        }
        return "";
    }

    public String getUrl() {
        return this.url;
    }

    public JsonObject getJson() {
        return this.json;
    }

    public void setJson(JsonObject json) {
        this.json = json;
    }
}