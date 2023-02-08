package app.sten.wit;

import static android.content.Context.NOTIFICATION_SERVICE;
import static android.provider.Settings.System.getString;
import static android.support.v4.content.ContextCompat.getSystemService;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

public class NotificationCreator {

    private static final String CHANNEL_ID = "Call ID Def";

    private static NotificationManager notificationManager;


    public NotificationCreator(NotificationManager notificationManager) {
        NotificationCreator.notificationManager = notificationManager;
    }

    public void Send(Context ctx, String title, String text) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(ctx)
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setContentTitle(title)
                        .setContentText(text)
                        .setPriority(Notification.PRIORITY_MAX)
                        .setDefaults(Notification.DEFAULT_VIBRATE)
                        .setVibrate(new long[0]);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "Channel human readable title", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);

            mBuilder.setChannelId(CHANNEL_ID);
        }
        notificationManager.notify(0, mBuilder.build());
    }
}