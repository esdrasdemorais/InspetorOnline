package com.esdrasmorais.inspetoronline.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.esdrasmorais.inspetoronline.data.model.Company;
import com.esdrasmorais.inspetoronline.data.model.Line;
import com.esdrasmorais.inspetoronline.data.model.Vehicle;

import java.util.List;

@Dao
public interface VehicleDao {
    @Query("SELECT * FROM Vehicle")
    LiveData<List<Vehicle>> getAll();

    @Query("SELECT * FROM Vehicle WHERE id MATCH :id LIMIT 1")
    LiveData<Vehicle> findById(String id);

    @Query("SELECT * FROM Vehicle WHERE id IN (:vehicleIds)")
    LiveData<List<Vehicle>> loadAllByIds(String[] vehicleIds);

    @Query("SELECT * FROM Vehicle WHERE prefix " +
            "MATCH :prefix LIMIT 1")
    LiveData<Vehicle> findByPrefix(String prefix);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Vehicle> vehicles); //void insertAll(Company... companies);

    @Delete
    void delete(Vehicle vehicle);

    @Update
    void update(Vehicle vehicle);

    @Query("DELETE FROM Vehicle WHERE id = :id")
    void delete(String id);

    @Query("DELETE FROM Vehicle")
    void removeAll();
}
