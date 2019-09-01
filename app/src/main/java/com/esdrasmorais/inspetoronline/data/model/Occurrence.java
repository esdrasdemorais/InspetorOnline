package com.esdrasmorais.inspetoronline.data.model;

import com.google.firebase.firestore.GeoPoint;

import java.util.Date;

public class Occurrence extends Default {
    private Inspection inspection;
    private Vehicle vehicle;
    private Line line;
    private Employee driver;
    private GeoPoint location;
    private OccurrenceType occurrenceType;
    private CollisionType collisionType;
    private AnalyzeType analyzeType;
    private Integer victimQuantity;
    private State state;
    private Date date;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Inspection getInspection() {
        return inspection;
    }

    public void setInspection(Inspection inspection) {
        this.inspection = inspection;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Line getLine() {
        return line;
    }

    public void setLine(Line line) {
        this.line = line;
    }

    public Employee getDriver() {
        return driver;
    }

    public void setDriver(Employee driver) {
        this.driver = driver;
    }

    public GeoPoint getLocation() {
        return location;
    }

    public void setLocation(GeoPoint location) {
        this.location = location;
    }

    public OccurrenceType getOccurrenceType() {
        return occurrenceType;
    }

    public void setOccurrenceType(OccurrenceType occurrenceType) {
        this.occurrenceType = occurrenceType;
    }

    public CollisionType getCollisionType() {
        return collisionType;
    }

    public void setCollisionType(CollisionType collisionType) {
        this.collisionType = collisionType;
    }

    public AnalyzeType getAnalyzeType() {
        return analyzeType;
    }

    public void setAnalyzeType(AnalyzeType analyzeType) {
        this.analyzeType = analyzeType;
    }

    public Integer getVictimQuantity() {
        return victimQuantity;
    }

    public void setVictimQuantity(Integer victimQuantity) {
        this.victimQuantity = victimQuantity;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}
