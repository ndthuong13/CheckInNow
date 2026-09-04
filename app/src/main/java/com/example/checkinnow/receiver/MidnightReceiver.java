package com.example.checkinnow.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import java.util.Calendar;

import com.example.checkinnow.database.AppDatabase;
import com.example.checkinnow.model.LichChamCong;
import com.example.checkinnow.util.AlarmManagerUtil;
import com.example.checkinnow.util.NhatKyManager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MidnightReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Context appContext = context.getApplicationContext();

        ExecutorService executor = Executors.newSingleThreadExecutor();

        executor.execute(() -> {

            AppDatabase database = AppDatabase.getDatabase(appContext);

            int thu = layThuHienTai();

            LichChamCong lich = database.lichChamCongDao().getByThu(thu);

            if (lich != null && lich.isEnabled()) {
                NhatKyManager.taoNhatKyNeuChuaCo(database.nhatKyChamCongDao(), lich);
            }
            executor.shutdown();

            AlarmManagerUtil.datAlarmNgayMoi(appContext);
        });
    }


    private int layThuHienTai() {
        Calendar calendar = java.util.Calendar.getInstance();

        int dayOfWeek = calendar.get(java.util.Calendar.DAY_OF_WEEK);

        switch (dayOfWeek) {
            case java.util.Calendar.MONDAY:
                return 1;
            case java.util.Calendar.TUESDAY:
                return 2;
            case java.util.Calendar.WEDNESDAY:
                return 3;
            case java.util.Calendar.THURSDAY:
                return 4;
            case java.util.Calendar.FRIDAY:
                return 5;
            case java.util.Calendar.SATURDAY:
                return 6;
            case java.util.Calendar.SUNDAY:
                return 7;
            default:
                return 1;
        }
    }
}