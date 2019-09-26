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

import java.util.List;

@Dao
public interface LineDao {
    @Query("SELECT * FROM Line")
    LiveData<List<Line>> getAll();

    @Query("SELECT * FROM Line WHERE id = :id LIMIT 1")
    LiveData<Line> findById(String id);

    @Query("SELECT * FROM Line WHERE id IN (:lineIds)")
    LiveData<List<Line>> loadAllByIds(int[] lineIds);

    @Query("SELECT * FROM Line WHERE shortName " +
            "LIKE :shortName LIMIT 1")
    LiveData<Line> findByName(String shortName);

    @Query("SELECT * FROM Line WHERE lineCode " +
            "= :lineCode LIMIT 1")
    LiveData<Line> findByLineCode(Integer lineCode);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Line> lines); //void insertAll(Company... companies);

    @Delete
    void delete(Line line);

    @Update
    void update(Line line);

    @Query("DELETE FROM Line WHERE id = :id")
    void delete(String id);

    @Query("DELETE FROM Line")
    void removeAll();
}
