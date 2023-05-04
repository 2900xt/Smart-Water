package com.remindrop.smartwater.ui.home;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.remindrop.smartwater.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<String> ozDrankText;
    private final MutableLiveData<String> nextReminderTime;
    private final MutableLiveData<String> currentDateText;
    private final MutableLiveData<Double> progressDrank;
    public double ouncesDrank, totalCapacity;

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

        double nextReminder = Math.round(JSONData.getDouble("nextReminderTime"));

        nextReminderTime.setValue("next in " + nextReminder + " minutes");

        totalCapacity = Math.round(JSONData.getDouble("totalCapacity") * 10) / 10.0;

        double waterDrank = JSONData.getJSONObject("waterConsumption").getDouble(LocalDate.now().toString());
        ouncesDrank = Math.round(waterDrank * 10) / 10.0;
        ozDrankText.setValue(ouncesDrank + " out of " + totalCapacity + " ounces drank today!");
        currentDateText.setValue("Today: " + LocalDate.now().toString());
        progressDrank.setValue(ouncesDrank / totalCapacity);
    }

    public LiveData<String> getWaterDrankText()
    {
        return ozDrankText;
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
        Util.saveWaterConsumption((int) ouncesDrank);
    }

}