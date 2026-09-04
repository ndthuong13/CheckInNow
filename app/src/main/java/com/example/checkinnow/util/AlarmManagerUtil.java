    package com.example.checkinnow.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.example.checkinnow.receiver.AlarmReceiver;

import java.util.Calendar;

public class AlarmManagerUtil {

    private AlarmManagerUtil() {
    }


    /**
     * Đặt alarm cho một ngày cụ thể.
     *
     * thu:
     * 1 = Thứ 2
     * ...
     * 7 = Chủ nhật
     */
    public static void datAlarm(
            Context context,
            int thu,
            String gio,
            String loai
    ) {

        if (gio == null) {
            return;
        }


        String[] parts =
                gio.split(":");

        if (parts.length != 2) {
            return;
        }


        int hour =
                Integer.parseInt(parts[0]);

        int minute =
                Integer.parseInt(parts[1]);


        Calendar calendar =
                Calendar.getInstance();


        int currentDay =
                chuyenCalendarSangThuUngDung(
                        calendar.get(
                                Calendar.DAY_OF_WEEK
                        )
                );


        int dayDifference =
                thu - currentDay;


        if (dayDifference < 0) {

            dayDifference += 7;

        } else if (
                dayDifference == 0
                        && !thoiGianConLai(
                        calendar,
                        hour,
                        minute
                )
        ) {

            // Giờ hôm nay đã trôi qua.
            // Đặt cho tuần sau.

            dayDifference = 7;
        }


        calendar.add(
                Calendar.DAY_OF_MONTH,
                dayDifference
        );


        calendar.set(
                Calendar.HOUR_OF_DAY,
                hour
        );

        calendar.set(
                Calendar.MINUTE,
                minute
        );

        calendar.set(
                Calendar.SECOND,
                0
        );

        calendar.set(
                Calendar.MILLISECOND,
                0
        );


        Intent intent =
                new Intent(
                        context,
                        AlarmReceiver.class
                );


        intent.putExtra(
                AlarmReceiver.EXTRA_LOAI,
                loai
        );


        intent.putExtra(
                AlarmReceiver.EXTRA_GIO,
                gio
        );

        intent.putExtra(
                AlarmReceiver.EXTRA_THU,
                thu
        );

        int requestCode =
                taoRequestCode(
                        thu,
                        loai
                );


        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(
                        context,
                        requestCode,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                                | PendingIntent.FLAG_IMMUTABLE
                );


        AlarmManager alarmManager =
                (AlarmManager)
                        context.getSystemService(
                                Context.ALARM_SERVICE
                        );


        if (alarmManager == null) {
            return;
        }


        if (Build.VERSION.SDK_INT >=
                Build.VERSION_CODES.S) {

            if (!alarmManager
                    .canScheduleExactAlarms()) {

                return;
            }
        }


        alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                pendingIntent
        );
    }


    /**
     * Kiểm tra giờ hôm nay còn chưa tới hay không.
     */
    private static boolean thoiGianConLai(
            Calendar current,
            int hour,
            int minute
    ) {

        Calendar target =
                (Calendar) current.clone();


        target.set(
                Calendar.HOUR_OF_DAY,
                hour
        );

        target.set(
                Calendar.MINUTE,
                minute
        );

        target.set(
                Calendar.SECOND,
                0
        );

        target.set(
                Calendar.MILLISECOND,
                0
        );


        return target.after(current);
    }


    /**
     * Calendar:
     *
     * Sunday = 1
     * Monday = 2
     * ...
     *
     * App:
     *
     * Monday = 1
     * ...
     * Sunday = 7
     */
    private static int
    chuyenCalendarSangThuUngDung(
            int dayOfWeek
    ) {

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

    public static void datAlarmTuanSau(
            Context context,
            int thu,
            String gio,
            String loai
    ) {

        if (gio == null) {
            return;
        }


        String[] parts =
                gio.split(":");

        if (parts.length != 2) {
            return;
        }


        int hour =
                Integer.parseInt(parts[0]);

        int minute =
                Integer.parseInt(parts[1]);


        Calendar calendar =
                Calendar.getInstance();


        calendar.add(
                Calendar.DAY_OF_MONTH,
                7
        );


        calendar.set(
                Calendar.HOUR_OF_DAY,
                hour
        );

        calendar.set(
                Calendar.MINUTE,
                minute
        );

        calendar.set(
                Calendar.SECOND,
                0
        );

        calendar.set(
                Calendar.MILLISECOND,
                0
        );


        Intent intent =
                new Intent(
                        context,
                        AlarmReceiver.class
                );


        intent.putExtra(
                AlarmReceiver.EXTRA_LOAI,
                loai
        );

        intent.putExtra(
                AlarmReceiver.EXTRA_GIO,
                gio
        );

        intent.putExtra(
                AlarmReceiver.EXTRA_THU,
                thu
        );


        int requestCode =
                taoRequestCode(
                        thu,
                        loai
                );


        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(
                        context,
                        requestCode,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                                | PendingIntent.FLAG_IMMUTABLE
                );


        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (alarmManager == null) {
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {

            if (!alarmManager.canScheduleExactAlarms()) {
                return;
            }
        }

        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    public static void datAlarmNgayMoi(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager == null) {
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!alarmManager.canScheduleExactAlarms()) {
                return;
            }
        }

        Calendar calendar = Calendar.getInstance();

        /*
         * Ngày tiếp theo
         */
        calendar.add(Calendar.DAY_OF_YEAR, 1);

        /*
         * Đặt đúng 00:00:00
         */
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);


        Intent intent = new Intent(context, com.example.checkinnow.receiver.MidnightReceiver.class);

        intent.setAction("com.example.checkinnow.ACTION_MIDNIGHT");


        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 9000, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);


        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }


    /**
     * Mỗi ngày + loại có một requestCode riêng.
     */
    private static int taoRequestCode(int thu, String loai) {
        if ("CHECK_IN".equals(loai)) {
            return 1000 + thu;
        } else {
            return 2000 + thu;
        }
    }
}