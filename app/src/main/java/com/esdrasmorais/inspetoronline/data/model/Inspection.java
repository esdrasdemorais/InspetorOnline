package com.esdrasmorais.inspetoronline.data.model;

import android.location.Location;

import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.GeoPoint;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Inspection extends Object {
    //Long id;
    Date startTime;
    Date endTime;
    GeoPoint address;
    String note;
    InspectionType type;
    WorkDaySchedule schedule;

    public Inspection() {
    }

    /*public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }*/

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Location getAddress() {
        Location location = new Location("");
        location.setLatitude(this.address.getLatitude());
        location.setLongitude(this.address.getLongitude());
        return location;
    }

    public void setAddress(Location location) {
        GeoPoint address = new GeoPoint(
            location.getLatitude(),
            location.getLongitude()
        );
        this.address = address;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public InspectionType getType() {
        return type;
    }

    public void setType(InspectionType type) {
        this.type = type;
    }

    public WorkDaySchedule getSchedule() {
        return schedule;
    }

    public void setSchedule(WorkDaySchedule schedule) {
        this.schedule = schedule;
    }

    public Map<String, Object> toObject() {
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("id", FieldValue.increment(1));
        objectMap.put("startTime", this.getStartTime());
        objectMap.put("endTime", this.getEndTime());
        objectMap.put("address", this.getAddress());
        objectMap.put("note", this.getNote());
        objectMap.put("type", this.type);
        objectMap.put("schedule", this.schedule);

        return objectMap;
    }
}