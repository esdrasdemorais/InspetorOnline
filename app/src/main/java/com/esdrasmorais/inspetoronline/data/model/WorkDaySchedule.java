package com.esdrasmorais.inspetoronline.data.model;

import android.location.Location;

import java.util.Date;

public class WorkDaySchedule extends Default {
//    private Long id;
    private Date startTime = new Date();
    private Date endTime = new Date();
    private Employee employee = new Employee();
    private Terminal terminal = new Terminal();

    public WorkDaySchedule() {
        Location location = new Location("");
        location.setLatitude(1.7);
        location.setLongitude(1.7);
        employee.setAddress(location);
    }

//    public Long getId() {
//        return id;
//    }

//    public void setId(Long id) {
//        this.id = id;
//    }

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

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Terminal getTerminal() {
        return terminal;
    }

    public void setTerminal(Terminal terminal) {
        this.terminal = terminal;
    }
}