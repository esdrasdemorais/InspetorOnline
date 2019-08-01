package com.esdrasmorais.inspetoronline.data.model;

import android.location.Location;

public class InspectionReport extends Default {
    private Long id;
    private Inspection inspection;
    private Line line;
    private Vehicle prefix;
    private Location location;
    private Rate vehicleState;
    private Rate presentationEmployees;
    private Boolean vehicleIdentification;
    private Rate personalObjectsCleaning;
    private Rate vehicleObjectsConservation;
    private Boolean employeeIdentification;
    private Boolean wheelchairSeatBelt;
    private Boolean objectsForbidenToRole;
    private Boolean vehicleSecurityAccessories;
    private Boolean impedimentToInspection;
    private Integer rate;
    private String comments;

    public InspectionReport() {

    }

//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }

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

    public Boolean getVehicleIdentification() {
        return vehicleIdentification;
    }

    public void setVehicleIdentification(Boolean vehicleIdentification) {
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

    public Boolean getEmployeeIdentification() {
        return employeeIdentification;
    }

    public void setEmployeeIdentification(Boolean employeeIdentification) {
        this.employeeIdentification = employeeIdentification;
    }

    public Boolean getWheelchairSeatBelt() {
        return wheelchairSeatBelt;
    }

    public void setWheelchairSeatBelt(Boolean wheelchairSeatBelt) {
        this.wheelchairSeatBelt = wheelchairSeatBelt;
    }

    public Boolean getObjectsForbidenToRole() {
        return objectsForbidenToRole;
    }

    public void setObjectsForbidenToRole(Boolean objectsForbidenToRole) {
        this.objectsForbidenToRole = objectsForbidenToRole;
    }

    public Boolean getVehicleSecurityAccessories() {
        return vehicleSecurityAccessories;
    }

    public void setVehicleSecurityAccessories(Boolean vehicleSecurityAccessories) {
        this.vehicleSecurityAccessories = vehicleSecurityAccessories;
    }

    public Boolean getImpedimentToInspection() {
        return impedimentToInspection;
    }

    public void setImpedimentToInspection(Boolean impedimentToInspection) {
        this.impedimentToInspection = impedimentToInspection;
    }

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}