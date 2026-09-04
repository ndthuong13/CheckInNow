package com.example.checkinnow.util;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.checkinnow.MainActivity;
import com.example.checkinnow.R;

public class NotificationUtil {

    private static final String CHANNEL_ID =
            "cham_cong_channel";


    private static final String CHANNEL_NAME =
            "Nhắc nhở chấm công";


    private NotificationUtil() {
    }


    /**
     * Tạo Notification Channel.
     */
    public static void createNotificationChannel(
            Context context
    ) {

        if (Build.VERSION.SDK_INT >=
                Build.VERSION_CODES.O) {

            NotificationChannel channel =
                    new NotificationChannel(
                            CHANNEL_ID,
                            CHANNEL_NAME,
                            NotificationManager
                                    .IMPORTANCE_HIGH
                    );

            channel.setDescription(
                    "Thông báo nhắc giờ chấm công"
            );


            NotificationManager manager =
                    context.getSystemService(
                            NotificationManager.class
                    );


            if (manager != null) {

                manager.createNotificationChannel(
                        channel
                );
            }
        }
    }


    public static void showCheckInNotification(
            Context context,
            String gio
    ) {

        showNotification(
                context,
                "Đến giờ Check-in",
                "Đã đến giờ Check-in"
                        + " (" + gio + ")",
                1001
        );
    }


    public static void showCheckOutNotification(
            Context context,
            String gio
    ) {

        showNotification(
                context,
                "Đến giờ Check-out",
                "Đã đến giờ Check-out"
                        + " (" + gio + ")",
                1002
        );
    }


    private static void showNotification(
            Context context,
            String title,
            String message,
            int notificationId
    ) {

        createNotificationChannel(context);


        Intent intent =
                new Intent(
                        context,
                        MainActivity.class
                );

        intent.setFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TOP
        );


        PendingIntent pendingIntent =
                PendingIntent.getActivity(
                        context,
                        notificationId,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                                | PendingIntent.FLAG_IMMUTABLE
                );


        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(
                        context,
                        CHANNEL_ID
                )
                        .setSmallIcon(
                                android.R.drawable.ic_dialog_info
                        )
                        .setContentTitle(title)
                        .setContentText(message)
                        .setStyle(
                                new NotificationCompat.BigTextStyle()
                                        .bigText(message)
                        )
                        .setPriority(
                                NotificationCompat.PRIORITY_HIGH
                        )
                        .setAutoCancel(true)
                        .setContentIntent(
                                pendingIntent
                        );


        if (Build.VERSION.SDK_INT >=
                Build.VERSION_CODES.TIRAMISU) {

            if (
                    ActivityCompat.checkSelfPermission(
                            context,
                            Manifest.permission.POST_NOTIFICATIONS
                    )
                            != PackageManager.PERMISSION_GRANTED
            ) {

                return;
            }
        }


        NotificationManagerCompat
                .from(context)
                .notify(
                        notificationId,
                        builder.build()
                );
    }
}