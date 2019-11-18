package com.esdrasmorais.inspetoronline.data.model;

public enum State {
    LIGHT("Leve", 1),
    SERIOUS("Grave", 2),
    CAPITAL("Gravíssima", 3),
    LOBBY("Portaria", 4),
    PREVENTIVE("Preventiva", 5),
    ANALYZE("Análise", 6);

    private String stringValue;
    private Integer intValue;

    private State(String stringValue, Integer intValue) {
        this.stringValue = stringValue;
        this.intValue = intValue;
    }

    @Override
    public String toString() {
        return this.stringValue;
    }

    public static State of(String state) {
        switch (state) {
            case "Leve": return LIGHT;
            case "Grave": return SERIOUS;
            case "Gravíssima": return CAPITAL;
            case "Portaria": return LOBBY;
            case "Preventiva": return PREVENTIVE;
            case "Análise": return ANALYZE;
            default: return null;
        }
    }
}
