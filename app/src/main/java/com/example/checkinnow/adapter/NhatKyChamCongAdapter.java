package com.example.checkinnow.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.checkinnow.R;
import com.example.checkinnow.model.NhatKyChamCong;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NhatKyChamCongAdapter
        extends RecyclerView.Adapter<NhatKyChamCongAdapter.ViewHolder> {

    private final List<NhatKyChamCong> danhSach;


    public NhatKyChamCongAdapter(
            List<NhatKyChamCong> danhSach
    ) {
        this.danhSach = danhSach;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        View view =
                LayoutInflater.from(
                        parent.getContext()
                ).inflate(
                        R.layout.item_nhat_ky,
                        parent,
                        false
                );

        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position
    ) {

        NhatKyChamCong nhatKy =
                danhSach.get(position);


        // Ngày

        holder.tvNgay.setText(
                dinhDangNgay(
                        nhatKy.getNgay()
                )
        );


        // Loại

        if ("CHECK_IN".equals(
                nhatKy.getLoai()
        )) {

            holder.tvLoai.setText(
                    "CHECK-IN"
            );

        } else {

            holder.tvLoai.setText(
                    "CHECK-OUT"
            );
        }


        // Giờ dự kiến

        holder.tvGioDuKien.setText(
                "Giờ dự kiến: "
                        + nhatKy.getGioDuKien()
        );


        // Trạng thái

        switch (nhatKy.getTrangThai()) {

            case "DA_CHAM":

                holder.tvTrangThai.setText(
                        "Đã chấm"
                );

                break;


            case "BO_LO":

                holder.tvTrangThai.setText(
                        "Bỏ lỡ"
                );

                break;


            case "CHUA_CHAM":

                holder.tvTrangThai.setText(
                        "Chưa chấm"
                );

                break;


            default:

                holder.tvTrangThai.setText(
                        nhatKy.getTrangThai()
                );

                break;
        }


        // Giờ thực tế

        if (nhatKy.getGioThucTe() != null
                && !nhatKy.getGioThucTe().isEmpty()) {

            holder.tvGioThucTe.setText(
                    "Giờ thực tế: "
                            + nhatKy.getGioThucTe()
            );

        } else {

            holder.tvGioThucTe.setText(
                    "Giờ thực tế: --:--"
            );
        }
    }


    @Override
    public int getItemCount() {
        return danhSach.size();
    }


    private String dinhDangNgay(
            String ngay
    ) {

        try {

            SimpleDateFormat input =
                    new SimpleDateFormat(
                            "yyyy-MM-dd",
                            Locale.getDefault()
                    );

            SimpleDateFormat output =
                    new SimpleDateFormat(
                            "dd/MM/yyyy",
                            Locale.getDefault()
                    );

            Date date =
                    input.parse(ngay);

            if (date != null) {
                return output.format(date);
            }

        } catch (ParseException e) {

            e.printStackTrace();
        }


        return ngay;
    }


    public static class ViewHolder
            extends RecyclerView.ViewHolder {

        TextView tvNgay;
        TextView tvLoai;
        TextView tvGioDuKien;
        TextView tvTrangThai;
        TextView tvGioThucTe;


        public ViewHolder(
                @NonNull View itemView
        ) {

            super(itemView);

            tvNgay =
                    itemView.findViewById(
                            R.id.tvNgay
                    );

            tvLoai =
                    itemView.findViewById(
                            R.id.tvLoai
                    );

            tvGioDuKien =
                    itemView.findViewById(
                            R.id.tvGioDuKien
                    );

            tvTrangThai =
                    itemView.findViewById(
                            R.id.tvTrangThai
                    );

            tvGioThucTe =
                    itemView.findViewById(
                            R.id.tvGioThucTe
                    );
        }
    }
}