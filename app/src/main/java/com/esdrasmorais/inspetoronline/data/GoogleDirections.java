package com.esdrasmorais.inspetoronline.data;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.util.Log;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.esdrasmorais.inspetoronline.data.interfaces.VolleyCallback;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GoogleDirections extends BaseObservable {
    @Bindable
    private JsonObject json;
    private RequestQueue requestQueue;
    private String url;
    private Context context;

    private String getGoogleApiKey() {
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(
                context.getPackageName(), PackageManager.GET_META_DATA
            );
            if (appInfo.metaData != null) {
                return appInfo.metaData.getString("com.google.android.geo.API_KEY");
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("GoogleDirections", e.getMessage());
        }
        return "";
    }

    public GoogleDirections(Context context, Location location) {
        this.context = context;

        url = "https://maps.googleapis.com/maps/api/directions/json?origin="+
            location.getLatitude()+","+location.getLongitude()+
            "&destination=Centro%20Guarulhos&avoid=highways|tolls|ferries&region=br" +
            "&departure_time=now&mode=transit&transit_mode=bus&key=" + this.getGoogleApiKey();

        //setGoogleDirections();
    }

    public String getUrl() {
        return this.url;
    }

    private void setGoogleDirections() {
        GetVolleyResponse getVolleyResponse = new GetVolleyResponse(context);
        getVolleyResponse.getResponse(Request.Method.GET, url, null,
            new GetVolleyResponse(context) {
                @Override
                public void onSuccessResponse(String result) {
                    try {
                        JSONObject response = new JSONObject(result);
//                        Snackbar.make(view, response.getString("message") +
//                         "", Snackbar.LENGTH_LONG)
//                              .setAction("Action", null).show();
                        json = new Gson().fromJson(result, JsonObject.class);
                        notifyPropertyChanged(BR.json);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        );
    }

    public JsonObject getJson() {
        return this.json;
    }

    public void setJson(JsonObject json) {
        this.json = json;
    }

    private void parseResponse() {
        StringRequest stringRequest = new StringRequest(
            Request.Method.GET, url,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    json = new Gson().fromJson(response, JsonObject.class);
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("GoogleDirection", error.getMessage());
                }
            }
        );
//        requestQueue = MySingleton.getInstance(
//            context.getApplicationContext()
//        ).getRequestQueue();
        MySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }

    public JsonElement getResponsibleBody() {
        return json.get("routes"); /*[0].legs[0].steps[1] != null ?
            json.routes[0].legs[0].steps[1].transit_details.line.agencies[0].name :
            "";*/
    }

    public String getLineName() {
        return /*json.routes[0].legs[0].steps[1] != null ?
            json.routes[0].legs[0].steps[1].transit_details.line.name :*/
            "";
    }

    public String getLineColor() {
        return /*json.routes[0].legs[0].steps[1] != null ?
            json.routes[0].legs[0].steps[1].transit_details.line.color :*/
            "";
    }

    public String getLineNumber() {
        return /*json.routes[0].legs[0].steps[1] != null ?
            json.routes[0].legs[0].steps[1].transit_details.line.color :*/
            "";
    }

    public String getHeadSign() {
        return /*json.routes[0].legs[0].steps[1] != null ?
            json.routes[0].legs[0].steps[1].transit_details.headsign :*/
            "";
    }

    public String getHeadWay() {
        return /*json.routes[0].legs[0].steps[1] != null ?
            json.routes[0].legs[0].steps[1].transit_details.headway :*/
            "";
    }

    public String getVehicleType() {
        return /*json.routes[0].legs[0].steps[1] != null ?
            json.routes[0].legs[0].steps[1].transit_details.line.vehicle.type :*/
            "";
    }

    public Integer getStopNumbers() {
        return /*json.routes[0].legs[0].steps[1] != null ?
            json.routes[0].legs[0].steps[1].transit_details.num_stops :*/
            0;
    }

    public List<Location> getBounds() {
        if (json.get("routes")/*[0]*/ == null)
            return null;
        List<Location> locations = new ArrayList<Location>();

        Location northeast = new Location("");
        /*northeast.setLatitude(json.routes[0].bounds.northeast.lat);
        northeast.setLongitude(json.routes[0].bounds.northeast.lng);
        locations.add(northeast);

        Location southwest = new Location();
        southwest.setLatitude(json.routes[0].bounds.southwest.lat);
        southwest.setLongitude(json.routes[0].bounds.southwest.lng);
        locations.add(southwest);*/

        return locations;
    }

    public Location getStartLocation() {
        /*if (json.routes[0].legs[0].steps[1] == null)
            return null;*/
        Location location = new Location("");
        /*location.setLatitude(
            json.routes[0].legs[0].steps[1].start_location.lat
        );
        location.setLongitude(
            json.routes[0].legs[0].steps[1].start_location.lng
        );*/
        return location;
    }

    public Location getEndLocation() {
        /*if (json.routes[0].legs[0].steps[1] == null)
            return null;*/
        Location location = new Location("");
        /*location.setLatitude(
            json.routes[0].legs[0].steps[1].end_location.lat
        );
        location.setLongitude(
            json.routes[0].legs[0].steps[1].end_location.lng
        );*/
        return location;
    }

    public String getWayPoints() {
        return /*json.routes[0].legs[0].steps[1] != null ?
            json.routes[0].legs[0].steps[1].polyline.points :*/
            "";
    }
}
