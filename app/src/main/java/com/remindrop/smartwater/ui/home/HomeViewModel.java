package com.remindrop.smartwater.ui.home;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.remindrop.smartwater.Util;

import org.json.JSONException;
import org.json.JSONObject;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<String> ozDrankText;
    private final MutableLiveData<String> nextReminderTime;
    private final MutableLiveData<String> currentDateText;
    private final MutableLiveData<Double> progressDrank;
    private double ouncesDrank, totalCapacity;

    public HomeViewModel()
    {
        nextReminderTime = new MutableLiveData<>();
        currentDateText = new MutableLiveData<>();
        ozDrankText = new MutableLiveData<>();
        progressDrank = new MutableLiveData<>();
    }

    public void setData() throws JSONException
    {
        JSONObject JSONData = Util.getJSON();

        if (!JSONData.has("nextReminderTime"))
        {
            JSONData.put("nextReminderTime", 0);
        }

        if (!JSONData.has("waterDrank"))
        {
            JSONData.put("waterDrank", 0);
        }

        if (!JSONData.has("totalCapacity"))
        {
            JSONData.put("totalCapacity", 60);
        }

        double nextReminder = Math.round(JSONData.getDouble("nextReminderTime"));

        nextReminderTime.setValue("next in " + nextReminder + " minutes");

        totalCapacity = Math.round(JSONData.getDouble("totalCapacity") * 10) / 10.0;

        ouncesDrank = Math.round(JSONData.getDouble("waterDrank") * 10) / 10.0;
        ozDrankText.setValue(ouncesDrank + " out of " + totalCapacity + " ounces drank today!");
        currentDateText.setValue("Today: " + java.time.LocalDate.now().toString());

        progressDrank.setValue(ouncesDrank / totalCapacity);
    }

    public LiveData<String> getWaterDrankText()
    {
        return ozDrankText;
    }

    public LiveData<String> getNextRemText()
    {
        return nextReminderTime;
    }

    public LiveData<String> getDateText()
    {
        return currentDateText;
    }

    public  LiveData<Double> getProgress()
    {
        return progressDrank;
    }
    public void addWaterDrank(int ounces)
    {
        ouncesDrank += ounces;

        progressDrank.setValue(ouncesDrank / totalCapacity);
        ozDrankText.setValue(ouncesDrank + " out of " + totalCapacity + " ounces drank today!");

        try
        {
            Util.getJSON().put("waterDrank", ouncesDrank);
        } catch (Exception ignored){}
    }

}