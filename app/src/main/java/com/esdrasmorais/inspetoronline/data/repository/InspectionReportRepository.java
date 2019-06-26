package com.esdrasmorais.inspetoronline.data.repository;

import android.location.Location;

import com.esdrasmorais.inspetoronline.data.model.Inspection;
import com.esdrasmorais.inspetoronline.data.model.InspectionReport;

import java.util.List;

public class InspectionReportRepository
    extends Repository<InspectionReport>
    implements IInspectionRepository
{
    public InspectionReportRepository(Class<InspectionReport> clas) {
        super(clas);
    }

    @Override
    public List<Inspection> findByLocation(Location location) {
        return null;
    }
}