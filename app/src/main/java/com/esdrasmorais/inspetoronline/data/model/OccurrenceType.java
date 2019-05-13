package com.esdrasmorais.inspetoronline.data.model;

public enum OccurrenceType {
    KILLING("Atropelamento", 1),
    COLISION("Colisão", 2),
    ACCIDENTAL_FALL("Queda Acidental", 3);

    private String stringValue;
    private Integer intValue;

    private OccurrenceType(String stringValue, Integer intValue) {
        this.stringValue = stringValue;
        this.intValue = intValue;
    }

    @Override
    public String toString() {
        return this.stringValue;
    }
}
