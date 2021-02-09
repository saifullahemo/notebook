package com.example.notebook;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

public class NotificationPublisher extends BroadcastReceiver {
    public static String NOTIFICATION_ID = "notification-id";
    public static String NOTIFICATION = "notification";

    private NotificationManager notificationManager;

    public void onReceive(Context context, Intent intent) {
        String title = context.getString(R.string.title);
        if ( notificationManager == null) {
         notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            }

        int importance = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            importance = NotificationManager.IMPORTANCE_HIGH;
        }
        NotificationChannel mChannel = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mChannel = notificationManager.getNotificationChannel(NOTIFICATION_ID);
            }
            if (mChannel == null){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    mChannel = new NotificationChannel(NOTIFICATION_ID, title, importance);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    mChannel.enableVibration(true);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 300, 200, 400});
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    notificationManager.createNotificationChannel(mChannel);
                }
            }

        Notification notification = intent.getParcelableExtra(NOTIFICATION);
        int id = intent.getIntExtra(NOTIFICATION_ID, 0);
        notificationManager.notify(id, notification);
    }
}
