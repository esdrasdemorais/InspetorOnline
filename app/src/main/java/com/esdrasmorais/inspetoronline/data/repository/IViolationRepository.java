package com.esdrasmorais.inspetoronline.data.repository;

import android.location.Location;

import com.esdrasmorais.inspetoronline.data.model.Violation;

import java.util.List;

public interface IViolationRepository {
    public List<Violation> findByLocation(Location location);
}