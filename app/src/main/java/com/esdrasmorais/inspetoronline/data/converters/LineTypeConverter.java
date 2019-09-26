package com.esdrasmorais.inspetoronline.data.converters;

import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.esdrasmorais.inspetoronline.data.model.LineType;

public class LineTypeConverter {
    @TypeConverter
    public LineType toLineType(String lineType) {
        switch (lineType) {
            case "BS": return LineType.BUS;
            case "SW": return LineType.SUBWAY;
            case "TR": return LineType.TRAIN;
            case "TN": return LineType.TRAN;
            case "RL": return LineType.RAIL;
            default: return null;
        }
    }

    @TypeConverter
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
}