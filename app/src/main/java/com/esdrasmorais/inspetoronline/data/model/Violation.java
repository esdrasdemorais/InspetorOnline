package com.esdrasmorais.inspetoronline.data.model;

import android.location.Location;

import com.google.firebase.firestore.GeoPoint;

import java.util.Date;

public class Violation extends Default {
    private Inspection inspection;
    private ViolationType violationType;
    private Line line;
    private Vehicle prefix;
    private Employee employee;
    private WorkTime workTime;
    private State state;
    private Department department;
    private Date date;
    private GeoPoint address;
    private EmployeeType employeeType;

    public Violation() {

    }

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

    public ViolationType getViolationType() {
        return violationType;
    }

    public void setViolationType(ViolationType violationType) {
        this.violationType = violationType;
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

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public WorkTime getWorkTime() {
        return workTime;
    }

    public void setWorkTime(WorkTime workTime) {
        this.workTime = workTime;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
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

    public EmployeeType getEmployeeType() {
        return employeeType;
    }

    public void setEmployeeType(EmployeeType employeeType) {
        this.employeeType = employeeType;
    }
}