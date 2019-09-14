package com.esdrasmorais.inspetoronline.data.model;

public enum Rate {
    GREAT("Otimo", 1),
    GOOD("Bom", 2),
    REGULATE("Regular", 3),
    BAD("Ruim", 4);

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
