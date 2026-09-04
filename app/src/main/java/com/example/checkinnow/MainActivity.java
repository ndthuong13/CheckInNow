package com.example.checkinnow;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

import com.example.checkinnow.dao.NhatKyChamCongDao;
import com.example.checkinnow.database.AppDatabase;
import com.example.checkinnow.model.LichChamCong;
import com.example.checkinnow.model.NhatKyChamCong;
import com.example.checkinnow.ui.HistoryActivity;
import com.example.checkinnow.ui.ScheduleActivity;
import com.example.checkinnow.util.DateTimeUtil;
import com.example.checkinnow.util.NhatKyManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_NOTIFICATION_PERMISSION = 100;
    private final Handler dayChangeHandler = new Handler(Looper.getMainLooper());

    private String ngayDangHienThi = "";

    private TextView tvThu;
    private TextView tvNgay;

    private TextView tvGioCheckIn;
    private TextView tvTrangThaiCheckIn;

    private TextView tvGioCheckOut;
    private TextView tvTrangThaiCheckOut;

    private Button btnMoLich, btnCheckIn, btnCheckOut;

    private AppDatabase database;

    private final ExecutorService databaseExecutor = Executors.newSingleThreadExecutor();

    private final Runnable dayChangeRunnable = new Runnable() {

                @Override
                public void run() {
                    String ngayHienTai = DateTimeUtil.getToday();
                    if (!ngayHienTai.equals(ngayDangHienThi)) {
                        ngayDangHienThi = ngayHienTai;
                        loadLichHomNay();
                    }
                    // Kiểm tra lại sau 30 giây
                    dayChangeHandler.postDelayed(this, 30 * 1000);
                }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Ánh xạ View
        tvThu = findViewById(R.id.tvThu);
        tvNgay = findViewById(R.id.tvNgay);
        tvGioCheckIn = findViewById(R.id.tvGioCheckIn);
        tvTrangThaiCheckIn = findViewById(R.id.tvTrangThaiCheckIn);
        tvGioCheckOut = findViewById(R.id.tvGioCheckOut);
        tvTrangThaiCheckOut = findViewById(R.id.tvTrangThaiCheckOut);

        btnMoLich = findViewById(R.id.btnMoLich);
        btnCheckIn = findViewById(R.id.btnTestCheckIn);
        btnCheckOut = findViewById(R.id.btnTestCheckOut);

        // Database
        database = AppDatabase.getDatabase(this);

        hienThiNgayHienTai();
        loadLichHomNay();
        xinQuyenNotification();
        testFirestore();

        // Mở màn hình thiết lập lịch
        btnMoLich.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ScheduleActivity.class);
            startActivity(intent);
        });

        Button btnMoNhatKy = findViewById(R.id.btnMoNhatKy);

        btnMoNhatKy.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
            startActivity(intent);
        });


        btnCheckIn.setOnClickListener(v -> {
            databaseExecutor.execute(() -> {
                NhatKyManager.danhDauDaCham(database.nhatKyChamCongDao(), DateTimeUtil.getToday(), "CHECK_IN");
                runOnUiThread(() ->
                        loadLichHomNay()
                );
            });
        });

        btnCheckOut.setOnClickListener(v -> {

            databaseExecutor.execute(() -> {

                NhatKyManager.danhDauDaCham(
                        database.nhatKyChamCongDao(),
                        DateTimeUtil.getToday(),
                        "CHECK_OUT"
                );

                runOnUiThread(() ->
                        loadLichHomNay()
                );
            });
        });


    }


    /**
     * Hiển thị thứ và ngày hiện tại
     */
    private void hienThiNgayHienTai() {

        Calendar calendar =
                Calendar.getInstance();

        int dayOfWeek =
                calendar.get(Calendar.DAY_OF_WEEK);

        String tenThu;

        switch (dayOfWeek) {

            case Calendar.MONDAY:
                tenThu = "Thứ 2";
                break;

            case Calendar.TUESDAY:
                tenThu = "Thứ 3";
                break;

            case Calendar.WEDNESDAY:
                tenThu = "Thứ 4";
                break;

            case Calendar.THURSDAY:
                tenThu = "Thứ 5";
                break;

            case Calendar.FRIDAY:
                tenThu = "Thứ 6";
                break;

            case Calendar.SATURDAY:
                tenThu = "Thứ 7";
                break;

            case Calendar.SUNDAY:
                tenThu = "Chủ nhật";
                break;

            default:
                tenThu = "";
                break;
        }


        tvThu.setText(tenThu);


        SimpleDateFormat dateFormat =
                new SimpleDateFormat(
                        "dd/MM/yyyy",
                        Locale.getDefault()
                );

        String ngay =
                dateFormat.format(new Date());

        tvNgay.setText(ngay);
    }


    /**
     * Đọc lịch chấm công của hôm nay
     */
    private void loadLichHomNay() {

        int thu =
                layThuTheoUngDung();


        databaseExecutor.execute(() -> {

            LichChamCong lich =
                    database
                            .lichChamCongDao()
                            .getByThu(thu);


            // Tạo nhật ký hôm nay nếu có lịch

            if (lich != null) {

                NhatKyManager
                        .taoNhatKyNeuChuaCo(
                                database.nhatKyChamCongDao(),
                                lich
                        );
            }


            runOnUiThread(() -> {

                if (lich == null) {

                    hienThiKhongCoLich();

                } else {

                    hienThiLich(lich);
                }
            });
        });
    }


    /**
     * Chuyển Calendar.DAY_OF_WEEK
     * thành quy ước của ứng dụng:
     *
     * Thứ 2 = 1
     * Thứ 3 = 2
     * ...
     * Chủ nhật = 7
     */
    private int layThuTheoUngDung() {

        Calendar calendar =
                Calendar.getInstance();

        int dayOfWeek =
                calendar.get(Calendar.DAY_OF_WEEK);


        switch (dayOfWeek) {

            case Calendar.MONDAY:
                return 1;

            case Calendar.TUESDAY:
                return 2;

            case Calendar.WEDNESDAY:
                return 3;

            case Calendar.THURSDAY:
                return 4;

            case Calendar.FRIDAY:
                return 5;

            case Calendar.SATURDAY:
                return 6;

            case Calendar.SUNDAY:
                return 7;

            default:
                return 1;
        }
    }


    /**
     * Hiển thị lịch hôm nay
     */
    private void hienThiLich(
            LichChamCong lich
    ) {

        if (!lich.isEnabled()) {

            tvGioCheckIn.setText("--:--");
            tvGioCheckOut.setText("--:--");

            tvTrangThaiCheckIn.setText(
                    "Hôm nay nghỉ"
            );

            tvTrangThaiCheckOut.setText(
                    "Hôm nay nghỉ"
            );

            return;
        }


        String ngay =
                DateTimeUtil.getToday();


        databaseExecutor.execute(() -> {

            NhatKyChamCongDao dao =
                    database.nhatKyChamCongDao();


            NhatKyChamCong checkIn =
                    dao.getByNgayVaLoai(
                            ngay,
                            "CHECK_IN"
                    );


            NhatKyChamCong checkOut =
                    dao.getByNgayVaLoai(
                            ngay,
                            "CHECK_OUT"
                    );


            runOnUiThread(() -> {

                // CHECK-IN

                if (checkIn != null) {

                    tvGioCheckIn.setText(
                            checkIn.getGioDuKien()
                    );

                    tvTrangThaiCheckIn.setText(
                            layTrangThaiHienThi(
                                    checkIn
                            )
                    );

                } else {

                    tvGioCheckIn.setText("--:--");

                    tvTrangThaiCheckIn.setText(
                            "Chưa thiết lập"
                    );
                }


                // CHECK-OUT

                if (checkOut != null) {

                    tvGioCheckOut.setText(
                            checkOut.getGioDuKien()
                    );

                    tvTrangThaiCheckOut.setText(
                            layTrangThaiHienThi(
                                    checkOut
                            )
                    );

                } else {

                    tvGioCheckOut.setText("--:--");

                    tvTrangThaiCheckOut.setText(
                            "Chưa thiết lập"
                    );
                }
            });
        });
    }


    /**
     * Không có lịch
     */
    private void hienThiKhongCoLich() {

        tvGioCheckIn.setText("--:--");

        tvGioCheckOut.setText("--:--");

        tvTrangThaiCheckIn.setText(
                "Chưa thiết lập lịch"
        );

        tvTrangThaiCheckOut.setText(
                "Chưa thiết lập lịch"
        );
    }


    /**
     * Xác định trạng thái dựa trên giờ hiện tại
     */
    private String tinhTrangThaiGio(
            String gio
    ) {

        try {

            SimpleDateFormat format =
                    new SimpleDateFormat(
                            "HH:mm",
                            Locale.getDefault()
                    );


            Date gioDat =
                    format.parse(gio);

            Date gioHienTai =
                    format.parse(
                            format.format(
                                    new Date()
                            )
                    );


            if (gioDat == null ||
                    gioHienTai == null) {

                return "Không xác định";
            }


            if (gioHienTai.before(gioDat)) {

                return "Chưa đến giờ";

            } else {

                return "Đã đến giờ";
            }

        } catch (Exception e) {

            return "Không xác định";
        }
    }


    private String layTrangThaiHienThi(
            NhatKyChamCong nhatKy
    ) {

        if (nhatKy == null) {
            return "Chưa chấm";
        }


        switch (nhatKy.getTrangThai()) {

            case "DA_CHAM":

                if (nhatKy.getGioThucTe() != null) {

                    return "Đã chấm lúc "
                            + nhatKy.getGioThucTe();
                }

                return "Đã chấm";


            case "BO_LO":

                return "Bỏ lỡ";


            case "CHUA_CHAM":

                return "Chưa chấm";


            default:

                return "Chưa chấm";
        }
    }


    /**
     * Khi quay trở lại MainActivity
     * thì đọc lại lịch.
     */

    private void xinQuyenNotification() {

        if (Build.VERSION.SDK_INT >=
                Build.VERSION_CODES.TIRAMISU) {

            if (
                    ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.POST_NOTIFICATIONS
                    )
                            != PackageManager.PERMISSION_GRANTED
            ) {

                ActivityCompat.requestPermissions(
                        this,
                        new String[]{
                                Manifest.permission
                                        .POST_NOTIFICATIONS
                        },
                        REQUEST_NOTIFICATION_PERMISSION
                );
            }
        }
    }


    private void testFirestore() {

        NhatKyChamCong nhatKy =
                new NhatKyChamCong(
                        "2026-09-04",
                        "CHECK_IN",
                        "08:30",
                        "DA_CHAM",
                        "08:27"
                );
        
    }

    @Override
    protected void onResume() {
        super.onResume();

        ngayDangHienThi = DateTimeUtil.getToday();

        loadLichHomNay();

        dayChangeHandler.removeCallbacks(
                dayChangeRunnable
        );

        dayChangeHandler.postDelayed(dayChangeRunnable,30 * 1000
        );
    }


    @Override
    protected void onDestroy() {
        dayChangeHandler.removeCallbacks(dayChangeRunnable);
        super.onDestroy();

        databaseExecutor.shutdown();
    }
}