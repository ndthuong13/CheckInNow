package com.example.checkinnow.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "lich_cham_cong")
public class LichChamCong {

    @PrimaryKey(autoGenerate = true)
    private int thu;

    private String gioCheckIn;

    private String gioCheckOut;

    private boolean enabled;

    public LichChamCong(
            int thu,
            String gioCheckIn,
            String gioCheckOut,
            boolean enabled
    ) {
        this.thu = thu;
        this.gioCheckIn = gioCheckIn;
        this.gioCheckOut = gioCheckOut;
        this.enabled = enabled;
    }

    public int getThu() {
        return thu;
    }

    public void setThu(int thu) {
        this.thu = thu;
    }

    public String getGioCheckIn() {
        return gioCheckIn;
    }

    public void setGioCheckIn(String gioCheckIn) {
        this.gioCheckIn = gioCheckIn;
    }

    public String getGioCheckOut() {
        return gioCheckOut;
    }

    public void setGioCheckOut(String gioCheckOut) {
        this.gioCheckOut = gioCheckOut;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}