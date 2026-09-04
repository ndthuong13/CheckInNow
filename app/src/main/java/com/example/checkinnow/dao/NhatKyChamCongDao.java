package com.example.checkinnow.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.checkinnow.model.NhatKyChamCong;

import java.util.List;

@Dao
public interface NhatKyChamCongDao {

    @Insert
    long insert(NhatKyChamCong nhatKy);

    @Update
    void update(NhatKyChamCong nhatKy);

    @Query("SELECT * FROM nhat_ky_cham_cong WHERE ngay = :ngay AND loai = :loai LIMIT 1")
    NhatKyChamCong getByNgayVaLoai(
            String ngay,
            String loai
    );

    @Query("SELECT * FROM nhat_ky_cham_cong WHERE ngay = :ngay ORDER BY loai ASC")
    List<NhatKyChamCong> getByNgay(
            String ngay
    );

    @Query("SELECT * FROM nhat_ky_cham_cong ORDER BY ngay DESC, id DESC")
    List<NhatKyChamCong> getAll();

    @Query("DELETE FROM nhat_ky_cham_cong WHERE ngay = :ngay")
    void deleteByNgay(String ngay);

    @Query("SELECT * FROM nhat_ky_cham_cong WHERE ngay BETWEEN :tuNgay AND :denNgay ORDER BY ngay DESC, id ASC")
    List<NhatKyChamCong> getByKhoangNgay(
            String tuNgay,
            String denNgay
    );
}