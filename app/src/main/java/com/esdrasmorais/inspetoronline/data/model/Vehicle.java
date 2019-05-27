package com.esdrasmorais.inspetoronline.data.model;

import java.util.Date;

public class Vehicle {
    private long id;
    private Integer prefix;
    private Boolean isHandicappedAccessible;
    private Date localizatedAt;
    private Double latitude;
    private Double longitude;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Integer getPrefix() {
        return prefix;
    }

    public void setPrefix(Integer prefix) {
        this.prefix = prefix;
    }

    public Boolean getHandicappedAccessible() {
        return isHandicappedAccessible;
    }

    public void setHandicappedAccessible(Boolean handicappedAccessible) {
        isHandicappedAccessible = handicappedAccessible;
    }

    public Date getLocalizatedAt() {
        return localizatedAt;
    }

    public void setLocalizatedAt(Date localizatedAt) {
        this.localizatedAt = localizatedAt;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return String.format("%s - %s", this.prefix, this.localizatedAt);
    }
}