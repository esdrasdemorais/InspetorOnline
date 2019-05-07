package com.esdrasmorais.inspetoronline.data.model;

import android.location.Location;

public class Terminal {
    private Location location = new Location("Taboao");
    private String address = "Praca 8";
    private String city = "Guarulhos";
    private String state = "SP";

    public Terminal() {
        location.setLatitude(-23.435083);
        location.setLongitude(-46.4974829);
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
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
}