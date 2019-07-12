package com.esdrasmorais.inspetoronline.data.model;

import android.location.Location;

import com.google.firebase.firestore.GeoPoint;

import java.util.List;

public class Employee extends Default {
    private String id;
    private String username;
    private String password;
    private GeoPoint address;
    private EmployeeType type;
    private List<Line> lines;

    public Employee() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public List<Line> getLines() {
        return lines;
    }

    public void setLines(List<Line> lines) {
        this.lines = lines;
    }
}
