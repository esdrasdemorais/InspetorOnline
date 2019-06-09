package com.esdrasmorais.inspetoronline.data.repository;

import com.esdrasmorais.inspetoronline.data.model.Employee;

public interface IInspectionReportRepository {
    public InspectionRepository findByEmployee(Employee employee);
}
