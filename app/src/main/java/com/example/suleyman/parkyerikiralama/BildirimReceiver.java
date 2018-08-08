package com.example.suleyman.parkyerikiralama;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

/**
 * Created by suleyman on 5/16/2017 - 13:11
 */

public class BildirimReceiver extends BroadcastReceiver
{
    private static final int BIR_DAKIKA = 1*60*1000;
    private static final int BES_DAKIKA = 5*BIR_DAKIKA;
    private static final int ON_DAKIKA = 10*BIR_DAKIKA;
    private static final int ONBES_DAKIKA = 15*BIR_DAKIKA;

    @Override
    public void onReceive(Context context, Intent intent) {
        String tarih = ""+intent.getExtras().get("tarih");
        String bitis_saat = ""+intent.getExtras().get("bitis_saat");

        int yil = Integer.parseInt(tarih.trim().split("-")[0]);
        int ay = Integer.parseInt(tarih.trim().split("-")[1]);
        int gun = Integer.parseInt(tarih.trim().split("-")[2]);

        int saat = Integer.parseInt(bitis_saat.trim().split(":")[0]);
        int dakika = Integer.parseInt(bitis_saat.trim().split(":")[1]);

        Calendar calendar = Calendar.getInstance();
        calendar.set(yil,ay-1,gun);
        calendar.set(Calendar.HOUR_OF_DAY,saat);
        calendar.set(Calendar.MINUTE,dakika);
        calendar.set(Calendar.SECOND,00);

        AlarmManager alarmManager = (AlarmManager)context.
                getSystemService(Context.ALARM_SERVICE);
        Intent ıntent = new Intent(context,
                NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.
                getBroadcast(context,0,ıntent,PendingIntent.
                        FLAG_UPDATE_CURRENT);

        alarmManager.set(AlarmManager.RTC_WAKEUP
                ,calendar.getTimeInMillis() -
                        ON_DAKIKA,pendingIntent);
        //alarmManager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis() - ONBES_DAKIKA,pendingIntent);
        //alarmManager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis() - BES_DAKIKA,pendingIntent);
    }
}