package com.esdrasmorais.inspetoronline.data.model;

import androidx.room.TypeConverter;

public enum LineType {
    BUS("BS", 1),
    SUBWAY("SW", 2),
    TRAIN("TR", 3),
    TRAN("TN", 4),
    RAIL("RL", 5);

    private String stringValue;
    private Integer intValue;

    private LineType(String stringValue, Integer intValue) {
        this.stringValue = stringValue;
        this.intValue = intValue;
    }

    public String toString(LineType lineType) {
        switch (lineType) {
            case BUS: return "BS";
            case SUBWAY: return "SW";
            case TRAIN: return "TR";
            case TRAN: return "TN";
            case RAIL: return "RL";
            default: return null;
        }
    }

    public static LineType of(String lineType) {
        switch (lineType) {
            case "BS": return BUS;
            case "SW": return SUBWAY;
            case "TR": return TRAIN;
            case "TN": return TRAN;
            case "RL": return RAIL;
            default: return null;
        }
    }
}