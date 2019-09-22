package com.esdrasmorais.inspetoronline.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.esdrasmorais.inspetoronline.data.dao.CompanyDao;
import com.esdrasmorais.inspetoronline.data.model.Company;
import com.esdrasmorais.inspetoronline.data.model.Line;
import com.esdrasmorais.inspetoronline.data.model.Vehicle;

@Database(entities = {Company.class, Line.class, Vehicle.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract CompanyDao companyDao();
}
