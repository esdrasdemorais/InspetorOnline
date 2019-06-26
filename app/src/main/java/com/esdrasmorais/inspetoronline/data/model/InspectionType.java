package com.esdrasmorais.inspetoronline.data.model;

public enum InspectionType {
    PERMANENCE("Permanence", 1),
    GUIDANCE("Guidance", 2),
    VIOLATION("Violation", 3),
    OCCURRENCE("Occurrence", 4),
    INTERFERENCE_ROUNDABOUT("InterferenceRoundabout", 5),
    SMOKE("Smoke", 6),
    DELAY("Delay", 7),
    LOSS("Loss", 8),
    CLEANING("Cleaning", 9),
    HOLE("Hole", 10),
    REPORT("Report", 11);

    private String stringValue;
    private Integer intValue;

    private InspectionType(String stringValue, Integer intValue) {
        this.stringValue = stringValue;
        this.intValue = intValue;
    }

    @Override
    public String toString() {
        return this.stringValue;
    }
}
