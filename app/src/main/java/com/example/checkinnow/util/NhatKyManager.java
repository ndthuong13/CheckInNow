package com.example.checkinnow.util;

import com.example.checkinnow.dao.NhatKyChamCongDao;
import com.example.checkinnow.model.LichChamCong;
import com.example.checkinnow.model.NhatKyChamCong;

public class NhatKyManager {

    private NhatKyManager() {
    }


    public static void taoNhatKyNeuChuaCo(
            NhatKyChamCongDao dao,
            LichChamCong lich
    ) {

        if (lich == null) {
            return;
        }

        if (!lich.isEnabled()) {
            return;
        }


        String ngay =
                DateTimeUtil.getToday();


        // CHECK-IN

        if (lich.getGioCheckIn() != null) {

            NhatKyChamCong checkIn =
                    dao.getByNgayVaLoai(
                            ngay,
                            "CHECK_IN"
                    );

            if (checkIn == null) {

                NhatKyChamCong moi =
                        new NhatKyChamCong(
                                ngay,
                                "CHECK_IN",
                                lich.getGioCheckIn(),
                                "CHUA_CHAM",
                                null
                        );

                dao.insert(moi);
            }
        }


        // CHECK-OUT

        if (lich.getGioCheckOut() != null) {

            NhatKyChamCong checkOut =
                    dao.getByNgayVaLoai(
                            ngay,
                            "CHECK_OUT"
                    );

            if (checkOut == null) {

                NhatKyChamCong moi =
                        new NhatKyChamCong(
                                ngay,
                                "CHECK_OUT",
                                lich.getGioCheckOut(),
                                "CHUA_CHAM",
                                null
                        );

                dao.insert(moi);
            }
        }
    }

    public static void danhDauDaCham(
            NhatKyChamCongDao dao,
            String ngay,
            String loai
    ) {

        NhatKyChamCong nhatKy =
                dao.getByNgayVaLoai(
                        ngay,
                        loai
                );

        if (nhatKy == null) {
            return;
        }


        nhatKy.setTrangThai(
                "DA_CHAM"
        );

        nhatKy.setGioThucTe(
                DateTimeUtil.getCurrentTime()
        );


        dao.update(nhatKy);
    }
}