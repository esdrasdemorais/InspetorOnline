package com.esdrasmorais.inspetoronline.data.model;

import android.location.Location;

import com.google.firebase.firestore.GeoPoint;

public class Terminal extends Default {
//    private Long id;
    private String name;
    //private Location location = new Location("Taboao");
    private GeoPoint location;
    private String address = "Praca 8";
    private String city = "Guarulhos";
    private String state = "SP";

    public Terminal() {
        Location location = new Location("Taboao");
        location.setLatitude(-23.435083);
        location.setLongitude(-46.4974829);
        GeoPoint address = new GeoPoint(
            location.getLatitude(),
            location.getLongitude()
        );
        this.location = address;
    }

//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }

    public Location getLocation() {
        Location location = new Location("");
        location.setLatitude(this.location.getLatitude());
        location.setLongitude(this.location.getLongitude());
        return location;
    }

    public void setLocation(Location location) {
        GeoPoint address = new GeoPoint(
            location.getLatitude(),
            location.getLongitude()
        );
        this.location = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}