package com.esdrasmorais.inspetoronline.data.model;

import android.location.Location;

import com.google.firebase.firestore.GeoPoint;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class Employee extends Default {
    //private String id;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("address")
    @Expose
    private GeoPoint address;
    @SerializedName("type")
    @Expose
    private EmployeeType type;
    @SerializedName("lines")
    @Expose
    private Map<Integer, Line> lines;

    public Employee() {

    }

//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Location getAddress() {
        Location address = new Location("");
        address.setLatitude(this.address.getLatitude());
        address.setLongitude(this.address.getLongitude());
        return address;
    }

    public void setAddress(Location address) {
        GeoPoint location = new GeoPoint(
            address.getLatitude(),
            address.getLongitude()
        );
        this.address = location;
    }

    public EmployeeType getType() {
        return type;
    }

    public void setType(EmployeeType type) {
        this.type = type;
    }

    public Map<Integer, Line> getLines() {
        return lines;
    }

    public void setLines(Map<Integer, Line> lines) {
        this.lines = lines;
    }

    @Override
    public String toString() {
        return username + " - " + type;
    }
}