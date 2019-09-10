package com.esdrasmorais.inspetoronline.data.repository;

import android.location.Location;

import com.esdrasmorais.inspetoronline.data.model.Guidance;

import java.util.List;

public interface IGuidanceRepository {
    public List<Guidance> findByLocation(Location location);
}