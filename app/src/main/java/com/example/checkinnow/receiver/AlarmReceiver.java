package com.example.checkinnow.receiver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.checkinnow.util.AlarmManagerUtil;
import com.example.checkinnow.util.NotificationUtil;

public class AlarmReceiver extends BroadcastReceiver {

    public static final String EXTRA_LOAI =
            "loai";

    public static final String EXTRA_GIO =
            "gio";

    public static final String EXTRA_THU =
            "thu";

    @Override
    public void onReceive(
            Context context,
            Intent intent
    ) {

        String loai =
                intent.getStringExtra(
                        EXTRA_LOAI
                );

        String gio =
                intent.getStringExtra(
                        EXTRA_GIO
                );
        int thu =
                intent.getIntExtra(
                        EXTRA_THU,
                        -1
                );


        if (loai == null || gio ==null ||thu == -1 ){
            return;
        }


        if ("CHECK_IN".equals(loai)) {

            NotificationUtil
                    .showCheckInNotification(
                            context,
                            gio
                    );

        } else if ("CHECK_OUT".equals(loai)) {

            NotificationUtil
                    .showCheckOutNotification(
                            context,
                            gio
                    );
        }

        AlarmManagerUtil
                .datAlarmTuanSau(
                        context,
                        thu,
                        gio,
                        loai
                );
    }
}