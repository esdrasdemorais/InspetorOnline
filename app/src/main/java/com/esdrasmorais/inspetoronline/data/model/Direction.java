package com.esdrasmorais.inspetoronline.data.model;

public enum Direction {
    MAIN_TERMINAL("TP", 1),
    SECONDARY_TERMINAL("TS", 2);

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
}