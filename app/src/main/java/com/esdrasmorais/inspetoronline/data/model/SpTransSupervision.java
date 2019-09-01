package com.esdrasmorais.inspetoronline.data.model;

import com.google.firebase.firestore.GeoPoint;

import java.util.Date;

public class SpTransSupervision extends Default {
    private Inspection inspection;
    private Date initialDate;
    private Date finalDate;
    private GeoPoint address;
    private String supervisionType;
    private String agents;
    private String scientificResponsible;
    private String note;

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

    public void setAddress(GeoPoint address) {
        this.address = address;
    }

    public String getSupervisionType() {
        return supervisionType;
    }

    public void setSupervisionType(String supervisionType) {
        this.supervisionType = supervisionType;
    }

    public String getAgents() {
        return agents;
    }

    public void setAgents(String agents) {
        this.agents = agents;
    }

    public String getScientificResponsible() {
        return scientificResponsible;
    }

    public void setScientificResponsible(String scientificResponsible) {
        this.scientificResponsible = scientificResponsible;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
