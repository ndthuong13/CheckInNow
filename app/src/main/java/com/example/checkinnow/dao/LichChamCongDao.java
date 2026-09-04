package com.example.checkinnow.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.checkinnow.model.LichChamCong;

import java.util.List;

@Dao
public interface LichChamCongDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(LichChamCong lichChamCong);

    @Query("SELECT * FROM lich_cham_cong ORDER BY thu ASC")
    List<LichChamCong> getAll();

    @Query("SELECT * FROM lich_cham_cong WHERE thu = :thu LIMIT 1")
    LichChamCong getByThu(int thu);

    @Query("DELETE FROM lich_cham_cong")
    void deleteAll();

    @Query("SELECT COUNT(*) FROM lich_cham_cong")
    int count();
}