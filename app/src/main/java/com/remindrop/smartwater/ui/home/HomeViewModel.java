package com.remindrop.smartwater.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<String> ozDrankText;
    private final MutableLiveData<String> nextReminderTime;
    private final MutableLiveData<String> currentDateText;
    private final MutableLiveData<Integer> progressDrank;

    public HomeViewModel() {

        nextReminderTime = new MutableLiveData<>();
        currentDateText = new MutableLiveData<>();
        ozDrankText = new MutableLiveData<>();
        progressDrank = new MutableLiveData<>();
    }

    public void setData(double ouncesDrank, double totalCapacity, double minutesBeforeNextRem)
    {
        minutesBeforeNextRem = Math.round(minutesBeforeNextRem);
        nextReminderTime.setValue("next in " + minutesBeforeNextRem + " minutes");

        totalCapacity = Math.round(totalCapacity * 10) / 10.0;
        ouncesDrank = Math.round(ouncesDrank * 10) / 10.0;
        ozDrankText.setValue(ouncesDrank + " out of " + totalCapacity + " ounces drank");

        currentDateText.setValue("Today: " + java.time.LocalDate.now().toString());

        progressDrank.setValue((int)(ouncesDrank / totalCapacity));
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

    public  LiveData<Integer> getProgress()
    {
        return progressDrank;
    }
}