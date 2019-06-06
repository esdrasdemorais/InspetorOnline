package com.esdrasmorais.inspetoronline.data.model;

import android.location.Location;

public class InspectionReport {
    private Long id;
    private Inspection inspection;
    private Line line;
    private Vehicle prefix;
    private Location location;
    private Rate vehicleState;
    private Rate presentationEmployees;
    private Rate vehicleIdentification;
    private Rate personalObjectsCleaning;
    private Rate vehicleObjectsConservation;
    private Rate employeeIdentification;
    private Rate wheelchairSeatBelt;
    private Boolean objectsForbidenToRole;
    private Rate vehicleSecurityAccessories;
    private Boolean impedimentToInspection;
    private Short rate;
    private String comments;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Inspection getInspection() {
        return inspection;
    }

    public void setInspection(Inspection inspection) {
        this.inspection = inspection;
    }

    public Line getLine() {
        return line;
    }

    public void setLine(Line line) {
        this.line = line;
    }

    public Vehicle getPrefix() {
        return prefix;
    }

    public void setPrefix(Vehicle prefix) {
        this.prefix = prefix;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Rate getVehicleState() {
        return vehicleState;
    }

    public void setVehicleState(Rate vehicleState) {
        this.vehicleState = vehicleState;
    }

    public Rate getPresentationEmployees() {
        return presentationEmployees;
    }

    public void setPresentationEmployees(Rate presentationEmployees) {
        this.presentationEmployees = presentationEmployees;
    }

    public Rate getVehicleIdentification() {
        return vehicleIdentification;
    }

    public void setVehicleIdentification(Rate vehicleIdentification) {
        this.vehicleIdentification = vehicleIdentification;
    }

    public Rate getPersonalObjectsCleaning() {
        return personalObjectsCleaning;
    }

    public void setPersonalObjectsCleaning(Rate personalObjectsCleaning) {
        this.personalObjectsCleaning = personalObjectsCleaning;
    }

    public Rate getVehicleObjectsConservation() {
        return vehicleObjectsConservation;
    }

    public void setVehicleObjectsConservation(Rate vehicleObjectsConservation) {
        this.vehicleObjectsConservation = vehicleObjectsConservation;
    }

    public Rate getEmployeeIdentification() {
        return employeeIdentification;
    }

    public void setEmployeeIdentification(Rate employeeIdentification) {
        this.employeeIdentification = employeeIdentification;
    }

    public Rate getWheelchairSeatBelt() {
        return wheelchairSeatBelt;
    }

    public void setWheelchairSeatBelt(Rate wheelchairSeatBelt) {
        this.wheelchairSeatBelt = wheelchairSeatBelt;
    }

    public Boolean getObjectsForbidenToRole() {
        return objectsForbidenToRole;
    }

    public void setObjectsForbidenToRole(Boolean objectsForbidenToRole) {
        this.objectsForbidenToRole = objectsForbidenToRole;
    }

    public Rate getVehicleSecurityAccessories() {
        return vehicleSecurityAccessories;
    }

    public void setVehicleSecurityAccessories(Rate vehicleSecurityAccessories) {
        this.vehicleSecurityAccessories = vehicleSecurityAccessories;
    }

    public Boolean getImpedimentToInspection() {
        return impedimentToInspection;
    }

    public void setImpedimentToInspection(Boolean impedimentToInspection) {
        this.impedimentToInspection = impedimentToInspection;
    }

    public Short getRate() {
        return rate;
    }

    public void setRate(Short rate) {
        this.rate = rate;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}