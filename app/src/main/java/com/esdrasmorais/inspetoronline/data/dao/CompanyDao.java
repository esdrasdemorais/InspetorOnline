package com.esdrasmorais.inspetoronline.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.esdrasmorais.inspetoronline.data.model.Company;

import java.util.List;

@Dao
public interface CompanyDao {
    @Query("SELECT * FROM Company")
    List<Company> getAll();

    @Query("SELECT * FROM Company WHERE id = :id LIMIT 1")
    Company findById(String id);

    @Query("SELECT * FROM Company WHERE id IN (:companyIds)")
    List<Company> loadAllByIds(int[] companyIds);

    @Query("SELECT * FROM Company WHERE companyName " +
        "LIKE :companyName LIMIT 1")
    Company findByName(String companyName);

    @Query("SELECT * FROM Company WHERE companyReferenceCode " +
            "LIKE :companyReferenceCode LIMIT 1")
    Company findByCompanyReferenceCode(String companyReferenceCode);

    @Insert
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