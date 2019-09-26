package com.esdrasmorais.inspetoronline.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.esdrasmorais.inspetoronline.data.model.Company;

import java.util.List;

@Dao
public interface CompanyDao {
    @Query("SELECT * FROM Company")
    LiveData<List<Company>> getAll();

    @Query("SELECT * FROM Company WHERE id = :id LIMIT 1")
    LiveData<Company> findById(String id);

    @Query("SELECT * FROM Company WHERE id IN (:companyIds)")
    LiveData<List<Company>> loadAllByIds(int[] companyIds);

    @Query("SELECT * FROM Company WHERE companyName " +
        "LIKE :companyName LIMIT 1")
    LiveData<Company> findByName(String companyName);

    @Query("SELECT * FROM Company WHERE companyReferenceCode " +
            "= :companyReferenceCode LIMIT 1")
    LiveData<Company> findByCompanyReferenceCode(Integer companyReferenceCode);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Company> companies); //void insertAll(Company... companies);

    @Delete
    void delete(Company company);

    @Update
    void update(Company company);

    @Query("DELETE FROM Company WHERE id = :id")
    void delete(String id);

    @Query("DELETE FROM Company")
    void removeAll();
}