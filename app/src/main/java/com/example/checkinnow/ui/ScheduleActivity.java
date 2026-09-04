package com.example.checkinnow.ui;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.checkinnow.R;
import com.example.checkinnow.adapter.LichChamCongAdapter;
import com.example.checkinnow.database.AppDatabase;
import com.example.checkinnow.model.LichChamCong;
import com.example.checkinnow.util.AlarmManagerUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ScheduleActivity extends AppCompatActivity
        implements LichChamCongAdapter.OnTimeClickListener {

    private RecyclerView recyclerView;

    private Button btnLuuLich;

    private LichChamCongAdapter adapter;

    private AppDatabase database;

    private final List<LichChamCong> danhSach =
            new ArrayList<>();

    private final ExecutorService databaseExecutor =
            Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_schedule
        );

        recyclerView =
                findViewById(R.id.recyclerViewLich);

        btnLuuLich =
                findViewById(R.id.btnLuuLich);

        database =
                AppDatabase.getDatabase(this);

        adapter =
                new LichChamCongAdapter(
                        danhSach,
                        this
                );

        recyclerView.setLayoutManager(
                new LinearLayoutManager(this)
        );

        recyclerView.setAdapter(adapter);

        loadLich();

        btnLuuLich.setOnClickListener(v ->
                saveLich()
        );
    }

    private void loadLich() {

        databaseExecutor.execute(() -> {

            List<LichChamCong> lichTuDatabase =
                    database
                            .lichChamCongDao()
                            .getAll();

            runOnUiThread(() -> {

                danhSach.clear();

                if (lichTuDatabase.isEmpty()) {

                    taoDuLieuMacDinh();

                } else {

                    danhSach.addAll(
                            lichTuDatabase
                    );
                }

                adapter.notifyDataSetChanged();
            });
        });
    }

    private void taoDuLieuMacDinh() {

        for (int i = 1; i <= 7; i++) {

            boolean lamViec = i <= 5;

            String checkIn =
                    lamViec ? "08:00" : null;

            String checkOut =
                    lamViec ? "17:30" : null;

            danhSach.add(
                    new LichChamCong(
                            i,
                            checkIn,
                            checkOut,
                            lamViec
                    )
            );
        }
    }

    private void saveLich() {

        databaseExecutor.execute(() -> {

            for (LichChamCong lich : danhSach) {

                database
                        .lichChamCongDao()
                        .insert(lich);
            }


            // Đặt lại toàn bộ alarm
            datTatCaAlarm();

            AlarmManagerUtil.datAlarmNgayMoi(
                    getApplicationContext()
            );


            runOnUiThread(() -> {

                Toast.makeText(
                        this,
                        "Đã lưu lịch chấm công",
                        Toast.LENGTH_SHORT
                ).show();
            });
        });
    }

    @Override
    public void onCheckInClick(int position) {

        hienThiTimePicker(
                position,
                true
        );
    }

    @Override
    public void onCheckOutClick(int position) {

        hienThiTimePicker(
                position,
                false
        );
    }

    private void hienThiTimePicker(
            int position,
            boolean isCheckIn
    ) {

        LichChamCong lich =
                danhSach.get(position);

        String gioHienTai =
                isCheckIn
                        ? lich.getGioCheckIn()
                        : lich.getGioCheckOut();

        int hour = 8;
        int minute = 0;

        if (gioHienTai != null) {

            String[] parts =
                    gioHienTai.split(":");

            hour =
                    Integer.parseInt(parts[0]);

            minute =
                    Integer.parseInt(parts[1]);
        }

        TimePickerDialog dialog =
                new TimePickerDialog(
                        this,
                        (view, hourOfDay, minuteOfHour) -> {

                            String time =
                                    String.format(
                                            Locale.getDefault(),
                                            "%02d:%02d",
                                            hourOfDay,
                                            minuteOfHour
                                    );

                            if (isCheckIn) {

                                lich.setGioCheckIn(time);

                            } else {

                                lich.setGioCheckOut(time);
                            }

                            adapter.notifyItemChanged(
                                    position
                            );
                        },
                        hour,
                        minute,
                        true
                );

        dialog.show();
    }

    private void datTatCaAlarm() {

        for (LichChamCong lich : danhSach) {

            if (!lich.isEnabled()) {
                continue;
            }


            if (lich.getGioCheckIn() != null) {

                AlarmManagerUtil.datAlarm(
                        this,
                        lich.getThu(),
                        lich.getGioCheckIn(),
                        "CHECK_IN"
                );
            }


            if (lich.getGioCheckOut() != null) {

                AlarmManagerUtil.datAlarm(
                        this,
                        lich.getThu(),
                        lich.getGioCheckOut(),
                        "CHECK_OUT"
                );
            }
        }
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();

        databaseExecutor.shutdown();
    }
}