package com.esdrasmorais.inspetoronline.data.model;

import androidx.annotation.NonNull;
import androidx.room.PrimaryKey;

public abstract class Default extends Object {
    @PrimaryKey
    @NonNull
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public abstract void log(String message);
}
