package com.example.checkinnow.ui;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.checkinnow.R;
import com.example.checkinnow.adapter.NhatKyChamCongAdapter;
import com.example.checkinnow.database.AppDatabase;
import com.example.checkinnow.model.NhatKyChamCong;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Spinner spinnerThang;

    private NhatKyChamCongAdapter adapter;
    private AppDatabase database;

    private final List<NhatKyChamCong> danhSach = new ArrayList<>();
    private final ExecutorService databaseExecutor = Executors.newSingleThreadExecutor();

    private final List<String> danhSachThang = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        recyclerView = findViewById(R.id.recyclerViewNhatKy);
        spinnerThang = findViewById(R.id.spinnerThang);
        database = AppDatabase.getDatabase(this);
        adapter = new NhatKyChamCongAdapter(danhSach);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        loadNhatKy();
        taoDanhSachThang();

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,danhSachThang);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerThang.setAdapter(spinnerAdapter);

        // Khi chọn tháng
        spinnerThang.setOnItemSelectedListener(
                new android.widget.AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                        String thang = danhSachThang.get(position);
                        loadNhatKyTheoThang(thang);
                    }
                    @Override
                    public void onNothingSelected(android.widget.AdapterView<?> parent) {
                    }
                }
        );
    }

    private void taoDanhSachThang() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("MM/yyyy", Locale.getDefault());
        danhSachThang.clear();

        for (int i = 0; i < 12; i++) {
            danhSachThang.add(format.format(calendar.getTime()));

            calendar.add(Calendar.MONTH, -1);
        }
    }

    private void loadNhatKyTheoThang(String thangNam) {
        String[] parts = thangNam.split("/");
        int thang = Integer.parseInt(parts[0]);
        int nam = Integer.parseInt(parts[1]);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, nam);
        calendar.set(Calendar.MONTH, thang - 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        String tuNgay = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.getTime());
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        String denNgay = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.getTime());

        databaseExecutor.execute(() -> {
            List<NhatKyChamCong> ketQua = database.nhatKyChamCongDao().getByKhoangNgay(tuNgay, denNgay);
            runOnUiThread(() -> {
                danhSach.clear();
                danhSach.addAll(ketQua);
                adapter.notifyDataSetChanged();
            });
        });
    }


    private void loadNhatKy() {
        databaseExecutor.execute(() -> {
            List<NhatKyChamCong> ketQua = database.nhatKyChamCongDao().getAll();
            runOnUiThread(() -> {
                danhSach.clear();
                danhSach.addAll(ketQua);
                adapter.notifyDataSetChanged();
            });
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        databaseExecutor.shutdown();
    }
}