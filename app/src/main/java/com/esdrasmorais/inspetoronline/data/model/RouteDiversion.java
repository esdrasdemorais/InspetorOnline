package com.esdrasmorais.inspetoronline.data.model;

import com.google.firebase.firestore.GeoPoint;

import java.util.Date;

public class RouteDiversion extends Default {
    private Inspection inspection;
    private Date date;
    private GeoPoint address;
    private String note;
    private ResponsibleBody responsibleBody;
    private String clerk;
    private String protocol;
    private String cocAware;

    public Inspection getInspection() {
        return inspection;
    }

    public void setInspection(Inspection inspection) {
        this.inspection = inspection;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public GeoPoint getAddress() {
        return address;
    }

    public void setAddress(GeoPoint address) {
        this.address = address;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public ResponsibleBody getResponsibleBody() {
        return responsibleBody;
    }

    public void setResponsibleBody(ResponsibleBody responsibleBody) {
        this.responsibleBody = responsibleBody;
    }

    public String getClerk() {
        return clerk;
    }

    public void setClerk(String clerk) {
        this.clerk = clerk;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getCocAware() {
        return cocAware;
    }

    public void setCocAware(String cocAware) {
        this.cocAware = cocAware;
    }
}
