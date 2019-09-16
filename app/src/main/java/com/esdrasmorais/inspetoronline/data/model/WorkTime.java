package com.esdrasmorais.inspetoronline.data.model;

public enum WorkTime {
    MORNING("Manhã", 1),
    AFTERNOON("Tarde", 2),
    NIGHT("Noite", 3),
    DAWN("Madrugada", 4);

    private String stringValue;
    private Integer intValue;

    private WorkTime(String stringValue, Integer intValue) {
        this.stringValue = stringValue;
        this.intValue = intValue;
    }

    @Override
    public String toString() {
        return this.stringValue;
    }

    public static WorkTime of(String workTime) {
        switch (workTime) {
            case "Manhã": return MORNING;
            case "Tarde": return AFTERNOON;
            case "Noite": return NIGHT;
            case "Madrugada": return DAWN;
            default: return null;
        }
    }
}
