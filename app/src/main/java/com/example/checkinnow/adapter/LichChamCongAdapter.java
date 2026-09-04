package com.example.checkinnow.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.checkinnow.R;
import com.example.checkinnow.model.LichChamCong;

import java.util.List;

public class LichChamCongAdapter extends RecyclerView.Adapter<LichChamCongAdapter.ViewHolder> {

    private final List<LichChamCong> danhSach;

    private final OnTimeClickListener listener;

    public interface OnTimeClickListener {
        void onCheckInClick(int position);

        void onCheckOutClick(int position);
    }

    public LichChamCongAdapter(List<LichChamCong> danhSach, OnTimeClickListener listener) {
        this.danhSach = danhSach;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lich_cham_cong, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LichChamCong lich = danhSach.get(position);

        holder.tvThu.setText(getTenThu(lich.getThu()));
        holder.btnCheckIn.setText(lich.getGioCheckIn() == null ? "--:--" : lich.getGioCheckIn());
        holder.btnCheckOut.setText(lich.getGioCheckOut() == null ? "--:--" : lich.getGioCheckOut());
        holder.switchEnabled.setChecked(lich.isEnabled());
        holder.btnCheckIn.setOnClickListener(v -> listener.onCheckInClick(position));
        holder.btnCheckOut.setOnClickListener(v -> listener.onCheckOutClick(position));
        holder.switchEnabled.setOnCheckedChangeListener(null);
        holder.switchEnabled.setChecked(lich.isEnabled());
        holder.switchEnabled.setOnCheckedChangeListener((buttonView, isChecked) -> lich.setEnabled(isChecked));
    }

    @Override
    public int getItemCount() {
        return danhSach.size();
    }

    private String getTenThu(int thu) {
        switch (thu) {
            case 1:
                return "Thứ 2";

            case 2:
                return "Thứ 3";

            case 3:
                return "Thứ 4";

            case 4:
                return "Thứ 5";

            case 5:
                return "Thứ 6";

            case 6:
                return "Thứ 7";

            case 7:
                return "Chủ nhật";

            default:
                return "Không xác định";
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvThu;
        Button btnCheckIn;
        Button btnCheckOut;
        Switch switchEnabled;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvThu = itemView.findViewById(R.id.tvThu);
            btnCheckIn = itemView.findViewById(R.id.btnCheckIn);
            btnCheckOut = itemView.findViewById(R.id.btnCheckOut);
            switchEnabled = itemView.findViewById(R.id.switchEnabled);
        }
    }
}