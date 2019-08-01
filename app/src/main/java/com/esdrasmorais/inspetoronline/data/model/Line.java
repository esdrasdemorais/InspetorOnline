package com.esdrasmorais.inspetoronline.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Line extends Default {
    //private Long id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("shortName")
    @Expose
    private String shortName;
    @SerializedName("type")
    @Expose
    private LineType type;
    @SerializedName("lineCode")
    @Expose
    private Integer lineCode;
    @SerializedName("direction")
    @Expose
    private Direction direction;
    @SerializedName("lineDestinationMarker")
    @Expose
    private String lineDestinationMarker;
    @SerializedName("lineOriginMarker")
    @Expose
    private String lineOriginMarker;
    @SerializedName("vehiclesQuantityLocalized")
    @Expose
    private Integer vehiclesQuantityLocalized;

    public Line() {

    }

//    public Long getId() {
//        return id;
//    }

//    public void setId(Long id) {
//        this.id = id;
//    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public LineType getType() {
        return type;
    }

    public void setType(LineType type) {
        this.type = type;
    }

    public Integer getLineCode() {
        return lineCode;
    }

    public void setLineCode(Integer lineCode) {
        this.lineCode = lineCode;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public String getLineDestinationMarker() {
        return lineDestinationMarker;
    }

    public void setLineDestinationMarker(String lineDestinationMarker) {
        this.lineDestinationMarker = lineDestinationMarker;
    }

    public String getLineOriginMarker() {
        return lineOriginMarker;
    }

    public void setLineOriginMarker(String lineOriginMarker) {
        this.lineOriginMarker = lineOriginMarker;
    }

    public Integer getVehiclesQuantityLocalized() {
        return vehiclesQuantityLocalized;
    }

    public void setVehiclesQuantityLocalized(Integer vehiclesQuantityLocalized) {
        this.vehiclesQuantityLocalized = vehiclesQuantityLocalized;
    }

    @Override
    public String toString() {
        return String.format("%s - %s", this.shortName, this.name);
    }
}
