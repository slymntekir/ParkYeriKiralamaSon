package com.example.suleyman.parkyerikiralama;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;

/**
 * Created by suleyman on 5/16/2017.
 */

public class NotificationReceiver extends BroadcastReceiver {
    private static final int NOTIFY_ID = 38;

    @Override
    public void onReceive(Context context, Intent i) {
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(context.NOTIFICATION_SERVICE);
        Intent ıntent = new Intent(context,SecimActivity.class);
        ıntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,NOTIFY_ID,ıntent,0);

        NotificationCompat.Builder builder = (NotificationCompat.Builder) new NotificationCompat.Builder(context).
                setSmallIcon(R.mipmap.ic_launcher).
                setContentTitle("Park Yeri Uyarı").
                setContentText("Lütfen Aracınızını Süresi Geçmeden Park Yerinden Kaldırınız..!!").
                setContentInfo("Süreniz Dolmak Üzere..!!").
                setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE).
                setContentIntent(pendingIntent);

        notificationManager.notify(NOTIFY_ID,builder.build());
    }
}