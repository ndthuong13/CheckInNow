package com.example.checkinnow.util;

import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateTimeUtil {
    private static final String TIME_ZONE = "Asia/Ho_Chi_Minh";

    private DateTimeUtil() {
    }


    /**
     * Ngày hiện tại:
     * yyyy-MM-dd
     */
    public static String getToday() {



        SimpleDateFormat format =
                new SimpleDateFormat(
                        "yyyy-MM-dd",
                        Locale.getDefault()
                );
        format.setTimeZone(TimeZone.getTimeZone(TIME_ZONE));

        return format.format(
                new Date()
        );
    }


    /**
     * Thời gian hiện tại:
     * HH:mm
     */
    public static String getCurrentTime() {

        SimpleDateFormat format =
                new SimpleDateFormat(
                        "HH:mm",
                        Locale.getDefault()
                );
        format.setTimeZone(TimeZone.getTimeZone(TIME_ZONE));
        return format.format(
                new Date()
        );
    }
}