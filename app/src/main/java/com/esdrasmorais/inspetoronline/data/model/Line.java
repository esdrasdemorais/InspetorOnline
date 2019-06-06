package com.esdrasmorais.inspetoronline.data.model;

public class Line {
    private Long id;
    private String name;
    private String shortName;
    private LineType type;
    private Integer lineCode;
    private Direction direction;
    private String lineDestinationMarker;
    private String lineOriginMarker;
    private Integer vehiclesQuantityLocalized;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
