package com.esdrasmorais.inspetoronline.data.model;

import android.location.Location;

import com.google.firebase.firestore.GeoPoint;

import java.util.Date;

public class Permanence extends Default {
    private Inspection inspection;
    private Date initialDate;
    private Date finalDate;
    private GeoPoint address;
    private String district;
    private String note;
    private Department department;
    private Integer infringementQuantity;

    public Inspection getInspection() {
        return inspection;
    }

    public void setInspection(Inspection inspection) {
        this.inspection = inspection;
    }

    public Date getInitialDate() {
        return initialDate;
    }

    public void setInitialDate(Date initialDate) {
        this.initialDate = initialDate;
    }

    public Date getFinalDate() {
        return finalDate;
    }

    public void setFinalDate(Date finalDate) {
        this.finalDate = finalDate;
    }

    public GeoPoint getAddress() {
        return address;
    }

    public void setAddress(Location location) {
        if (location == null) return;
        GeoPoint address = new GeoPoint(
            location.getLatitude(),
            location.getLongitude()
        );
        this.address = address;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Integer getInfringementQuantity() {
        return infringementQuantity;
    }

    public void setInfringementQuantity(Integer infringementQuantity) {
        this.infringementQuantity = infringementQuantity;
    }
}
