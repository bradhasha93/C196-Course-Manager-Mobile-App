package wgu.com.bhasha.c196scheduler.receivers;

import android.app.NotificationChannel;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.Random;

import wgu.com.bhasha.c196scheduler.R;
import wgu.com.bhasha.c196scheduler.data.Assessment;
import wgu.com.bhasha.c196scheduler.managers.NotificationManager;

public class NotificationReceiver extends BroadcastReceiver {

    private static final String NOTIFICATION_CHANNEL_ID = "wgu.com.bhasha.c196scheduler.courseEndDate";
    private static final String NOTIFICATION_CHANNEL_NAME = "C196 Scheduler Notification0";
    public static int notificationId = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        final int icon = intent.getExtras().getInt("icon");
        final String title = intent.getExtras().getString("title");
        final String text = intent.getExtras().getString("text");
        displayAlert(context, icon, title, text);
    }

    private void displayAlert(Context context, int icon, final String title, final String text) {
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(icon)
                .setContentTitle(title)
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        android.app.NotificationManager notificationManager =
                (android.app.NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME,
                    android.app.NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(notificationId, notificationBuilder.build());
        notificationId++;
    }

}
