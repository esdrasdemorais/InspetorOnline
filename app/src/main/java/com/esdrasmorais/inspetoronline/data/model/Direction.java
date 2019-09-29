package com.esdrasmorais.inspetoronline.data.model;

public enum Direction {
    MAIN_TERMINAL("TP", 1),
    SECONDARY_TERMINAL("TS", 2),
    GARAGE("G", 3);

    private String stringValue;
    private Integer intValue;

    private Direction(String stringValue, Integer intValue) {
        this.stringValue = stringValue;
        this.intValue = intValue;
    }

    @Override
    public String toString() {
        return this.stringValue;
    }

    public static Direction of(String direction) {
        switch (direction) {
            case "TP" : case "1": return MAIN_TERMINAL;
            case "TS": case "2" : return SECONDARY_TERMINAL;
            case "G": case "3": return GARAGE;
            default: return null;
        }
    }
}