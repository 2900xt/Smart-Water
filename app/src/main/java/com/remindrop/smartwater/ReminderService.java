package com.remindrop.smartwater;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class ReminderService extends Service {
    public static void restartService(Context c)
    {
        if(c.getSystemService(ReminderService.class) != null)
        {
            c.stopService(new Intent(c, ReminderService.class));
        }

        JSONObject data = Util.getJSON();

        try {
            if(!data.getBoolean("remindersEnabled"))
            {
                return;
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        Intent reminderService = new Intent(c, ReminderService.class);
        try {
            reminderService.putExtra("Downtime Start", LocalTime.parse(data.getString("downtimeStart")));
            reminderService.putExtra("Downtime End", LocalTime.parse(data.getString("downtimeEnd")));
            reminderService.putExtra("Next Rem", LocalTime.now().plusSeconds(data.getInt("reminderInterval") * 60L));
            reminderService.putExtra("Rem Interval", data.getInt("reminderInterval"));
        } catch (Exception e)
        {
            e.printStackTrace();
            System.exit(-1);
        }

        c.startService(reminderService);

    }

    private Thread reminderServiceThread;
    private Notification notification;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();

        NotificationCompat.Builder notifBuilder = new NotificationCompat.Builder(this, "com.remindrop.smartwater")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Remindrop Reminder Service")
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        notification = notifBuilder.build();

        startForeground(100, notification);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        stopForeground(true);
    }

    @Override
    public int onStartCommand(Intent intent, int startFlags, int startID)
    {
        Bundle args = intent.getExtras();

        long reminderInterval = args.getInt("Rem Interval");
        LocalTime nextRem = (LocalTime) args.get("Next Rem");
        Duration timeTillNextRem = Duration.between(LocalTime.now(), nextRem);

        if(timeTillNextRem.isNegative())
        {
            timeTillNextRem = Duration.between(nextRem, LocalTime.now());
        }

        LocalTime downtimeStart = (LocalTime) args.get("Downtime Start");
        LocalTime downtimeEnd = (LocalTime) args.get("Downtime End");

        reminderServiceThread = new Thread(new ReminderServiceThread(timeTillNextRem.getSeconds() / 60, reminderInterval, downtimeStart, downtimeEnd));
        reminderServiceThread.start();
        return Service.START_STICKY;
    }

    private class ReminderServiceThread implements Runnable
    {
        private long minsTillNextRem, reminderInterval;
        private LocalDateTime downtimeStart;
        private LocalDateTime downtimeEnd;

        public ReminderServiceThread(long minsTillNextRem, long reminderInterval, LocalTime downtimeStart, LocalTime downtimeEnd)
        {
            this.reminderInterval = reminderInterval;
            this.minsTillNextRem = minsTillNextRem;

            setDowntime(downtimeStart, downtimeEnd);

            System.out.println(downtimeStart.getHour());
        }
        public void setDowntime(LocalTime downtimeStart, LocalTime downtimeEnd)
        {
            this.downtimeStart = downtimeStart.atDate(LocalDate.now());
            if(downtimeStart.isAfter(downtimeEnd))
            {
                this.downtimeEnd = downtimeEnd.atDate(LocalDate.now()).plus(Duration.ofDays(1));
            }
            else
            {
                this.downtimeEnd = LocalDateTime.from(downtimeEnd.atDate(LocalDate.now()));
            }
        }

        public void sleepDowntime()
        {
            Duration downtimeDuration = Duration.between(downtimeStart, downtimeEnd);
            System.out.println("Downtime for " + downtimeDuration.getSeconds());
            try
            {
                Thread.sleep(1000 * downtimeDuration.getSeconds());
            } catch (InterruptedException e)
            {
                Thread.currentThread().interrupt();
            }

            downtimeStart = downtimeStart.plus(Duration.ofDays(1));
            downtimeEnd = downtimeEnd.plus(Duration.ofDays(1));
        }

        @Override
        public void run() {
            while (Thread.currentThread().isAlive())
            {
                if(LocalDateTime.now().isAfter(downtimeStart) && LocalDateTime.now().isBefore(downtimeEnd))
                {
                    sleepDowntime();
                }

                try
                {
                    System.out.println("Sleeping for: " + minsTillNextRem * 60 * 1000);
                    Thread.sleep( minsTillNextRem * 60 * 1000);
                    minsTillNextRem += reminderInterval;
                    Util.sendNotification("Drink Water!", "Water Reminders", getApplicationContext());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}
