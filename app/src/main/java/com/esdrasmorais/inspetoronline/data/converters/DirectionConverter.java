package com.esdrasmorais.inspetoronline.data.converters;

import androidx.room.TypeConverter;

import com.esdrasmorais.inspetoronline.data.model.Direction;

public class DirectionConverter {
    @TypeConverter
    public Direction toDirection(String direction) {
        switch (direction) {
            case "TP": return Direction.MAIN_TERMINAL;
            case "TS": return Direction.SECONDARY_TERMINAL;
            case "G": return Direction.GARAGE;
            default: return null;
        }
    }

    @TypeConverter
    public String toString(Direction direction) {
        switch (direction) {
            case MAIN_TERMINAL: return "TP";
            case SECONDARY_TERMINAL: return "TS";
            case GARAGE: return "G";
            default: return null;
        }
    }
}