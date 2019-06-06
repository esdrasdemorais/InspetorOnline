package com.esdrasmorais.inspetoronline.data.model;

import android.location.Location;

import java.util.Date;

public class Inspection {
    private Long id;
    private Date startTime;
    private Date endTime;
    private Location address;
    private String note;
    private InspectionType type;
    private WorkDaySchedule schedule;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
        return address;
    }

    public void setAddress(Location address) {
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
}
