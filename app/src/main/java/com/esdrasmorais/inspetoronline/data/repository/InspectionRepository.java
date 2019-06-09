package com.esdrasmorais.inspetoronline.data.repository;

import android.location.Location;

import com.esdrasmorais.inspetoronline.data.model.Inspection;

import java.util.List;

public class InspectionRepository
    extends Repository<Inspection>
    implements IInspectionRepository
{
    @Override
    public List<Inspection> findByLocation(Location location) {
        return null;
    }
}
