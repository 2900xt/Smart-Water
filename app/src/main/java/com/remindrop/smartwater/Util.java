package com.remindrop.smartwater;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Util {

    private static JSONObject JSONData;
    private static Context JSONDataContext;
    public static void swapFragments(Fragment currentFragment, Fragment newFragment) {
        FragmentTransaction fragmentTransaction = currentFragment.getParentFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment_activity_main, newFragment);
        fragmentTransaction.addToBackStack("Home");
        fragmentTransaction.commit();
    }

    public static void writeJSON(JSONObject data, String filename, Context context) {
        try {
            FileOutputStream fOS = context.openFileOutput(filename, Context.MODE_PRIVATE);
            OutputStreamWriter osWriter = new OutputStreamWriter(fOS);
            BufferedWriter writer = new BufferedWriter(osWriter);

            writer.write(data.toString());

            writer.close();
            osWriter.close();
            fOS.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public static JSONObject readJSON(String filename, Context context) {
        JSONObject data = null;
        try {
            FileInputStream fIS = context.openFileInput(filename);
            InputStreamReader isReader = new InputStreamReader(fIS);
            BufferedReader reader = new BufferedReader(isReader);

            StringBuilder jsonBuilder = new StringBuilder();
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                jsonBuilder.append(currentLine);
            }

            if (jsonBuilder.length() < 1) {
                jsonBuilder.append("{}");
            }

            data = new JSONObject(
                    new JSONTokener(jsonBuilder.toString())
            );

            reader.close();
            isReader.close();
            fIS.close();
        } catch (FileNotFoundException e) {
            try {
                FileOutputStream fOS = context.openFileOutput(filename, Context.MODE_PRIVATE);
                OutputStreamWriter osWriter = new OutputStreamWriter(fOS);
                BufferedWriter writer = new BufferedWriter(osWriter);

                writer.write("{}");

                writer.close();
                osWriter.close();
                fOS.close();
            } catch (Exception f) {
                f.printStackTrace();
                System.exit(-1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }

        return data;
    }

    public static void JSONInit(Context context) {
        JSONDataContext = context;
        JSONData = readJSON("remindropData.json", JSONDataContext);

        try {
            if (!JSONData.has("nextReminderTime")) {
                JSONData.put("nextReminderTime", 0);
            }

            if (!JSONData.has("totalCapacity")) {
                JSONData.put("totalCapacity", 60);
            }

            if(!JSONData.has("downtimeEnd"))
            {
                JSONData.put("downtimeEnd", "12:00");
            }

            if(!JSONData.has("remindersEnabled"))
            {
                JSONData.put("remindersEnabled", false);
            }

            if(!JSONData.has("downtimeStart"))
            {
                JSONData.put("downtimeStart", "12:00");
            }

            if(!JSONData.has("reminderInterval"))
            {
                JSONData.put("reminderInterval", 0);
            }

            JSONObject waterConsumption = null;
            if(!JSONData.has("waterConsumption"))
            {
                waterConsumption = new JSONObject();
                waterConsumption.put(LocalDate.now().toString(), 0);
                JSONData.put("waterConsumption", waterConsumption);
            } else
            {
                waterConsumption = JSONData.getJSONObject("waterConsumption");
                if(!waterConsumption.has(LocalDate.now().toString()))
                {
                    waterConsumption.put(LocalDate.now().toString(), 0);
                }
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public static void JSONSave() {
        writeJSON(JSONData, "remindropData.json", JSONDataContext);
    }

    public static JSONObject getJSON() {
        return JSONData;
    }

    public static void sendNotification(CharSequence message, CharSequence title, Context app) {

        /*Source: https://developer.android.com/develop/ui/views/notifications/build-notification*/
        //Ask for a notification form the system
        NotificationCompat.Builder notifBuilder = new NotificationCompat.Builder(app, "com.remindrop.smartwater")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat notifManager = NotificationManagerCompat.from(app.getApplicationContext());

        //Check if we have the ability to send notifications
        if (ActivityCompat.checkSelfPermission(app, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        //Send the notification
        notifManager.notify(1, notifBuilder.build());
    }

    public static void createNotificationChannel(AppCompatActivity app) {

        /*Source: https://developer.android.com/develop/ui/views/notifications/channels*/

        CharSequence name = app.getString(R.string.channel_name);
        String description = app.getString(R.string.channel_description);
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel channel = new NotificationChannel("com.remindrop.smartwater", name, importance);
        channel.setDescription(description);

        // Register the channel with the system. You can't change the importance
        // or other notification behaviors after this.
        NotificationManager notificationManager = app.getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }


    public static void saveWaterConsumption(int ouncesConsumed)
    {
        try {
            JSONObject waterConsumptionHistory = Util.getJSON().getJSONObject("waterConsumption");
            waterConsumptionHistory.put(LocalDate.now().toString(), ouncesConsumed);
            Util.getJSON().put("waterConsumption", waterConsumptionHistory);
        } catch (JSONException e)
        {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
