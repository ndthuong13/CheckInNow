package com.example.checkinnow.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "nhat_ky_cham_cong")
public class NhatKyChamCong {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String ngay;

    private String loai;

    private String gioDuKien;

    private String trangThai;

    private String gioThucTe;


    public NhatKyChamCong(String ngay, String loai, String gioDuKien, String trangThai, String gioThucTe) {
        this.ngay = ngay;
        this.loai = loai;
        this.gioDuKien = gioDuKien;
        this.trangThai = trangThai;
        this.gioThucTe = gioThucTe;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getNgay() {
        return ngay;
    }

    public void setNgay(String ngay) {
        this.ngay = ngay;
    }


    public String getLoai() {
        return loai;
    }

    public void setLoai(String loai) {
        this.loai = loai;
    }


    public String getGioDuKien() {
        return gioDuKien;
    }

    public void setGioDuKien(String gioDuKien) {
        this.gioDuKien = gioDuKien;
    }


    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }


    public String getGioThucTe() {
        return gioThucTe;
    }

    public void setGioThucTe(String gioThucTe) {
        this.gioThucTe = gioThucTe;
    }
}