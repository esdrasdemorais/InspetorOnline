package com.esdrasmorais.inspetoronline.data.model;

public enum AnalyzeType {
    WITH_FAULT("Com Culpa", 1),
    WITHOUT_FAULT("Sem Culpa", 2);

    private String stringValue;
    private Integer intValue;

    private AnalyzeType(String stringValue, Integer intValue) {
        this.stringValue = stringValue;
        this.intValue = intValue;
    }

    @Override
    public String toString() {
        return this.stringValue;
    }
}
