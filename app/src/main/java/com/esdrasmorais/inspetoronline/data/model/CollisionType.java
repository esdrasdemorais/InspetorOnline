package com.esdrasmorais.inspetoronline.data.model;

public enum CollisionType {
    FRONT("Frontal", 1),
    REAR("Traseira", 2),
    LEFT("Esquerda", 3),
    RIGHT("Direita", 4);

    private String stringValue;
    private Integer intValue;

    private CollisionType(String stringValue, Integer intValue) {
        this.stringValue = stringValue;
        this.intValue = intValue;
    }
}
