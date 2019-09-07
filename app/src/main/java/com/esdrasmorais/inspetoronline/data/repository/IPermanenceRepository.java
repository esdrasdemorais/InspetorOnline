package com.esdrasmorais.inspetoronline.data.repository;

import android.location.Location;

import com.esdrasmorais.inspetoronline.data.model.Permanence;

import java.util.List;

public interface IPermanenceRepository {
    public List<Permanence> findByLocation(Location location);
}
