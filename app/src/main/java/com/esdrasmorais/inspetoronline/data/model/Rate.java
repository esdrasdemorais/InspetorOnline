package com.esdrasmorais.inspetoronline.data.model;

public enum Rate {
    GREAT("Great", 1),
    GOOD("Good", 2),
    REGULATE("Regulate", 3),
    BAD("Bad", 4);

    private String stringValue;
    private Integer intValue;

    private Rate(String stringValue, Integer intValue) {
        this.stringValue = stringValue;
        this.intValue = intValue;
    }

    @Override
    public String toString() {
        return this.stringValue;
    }
}
