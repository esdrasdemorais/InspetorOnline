package com.esdrasmorais.inspetoronline.data.repository;

import android.location.Location;

import com.esdrasmorais.inspetoronline.data.model.Inspection;

import java.util.List;

public interface IInspectionRepository {
    public List<Inspection> findByLocation(Location location);
}