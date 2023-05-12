package com.remindrop.smartwater.ui.goal;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class WaterGoalViewModel extends ViewModel {

    private final MutableLiveData<String> mText;
    private static final double EXCERCIZE_FACTOR = 12.0 / 30.0;
    private static final double WEIGHT_FACTOR = 2.0 / 3.0;
    public double optimalGoal;


    public WaterGoalViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Set Goal: 0 oz");
        optimalGoal = 0;
    }

    public double calculateOptimalGoal(double weight, double minExcercise)
    {
        double ouncesToDrink =  (weight * WEIGHT_FACTOR) + (minExcercise * EXCERCIZE_FACTOR);
        ouncesToDrink = Math.round(ouncesToDrink * 10) / 10.0;
        mText.setValue("Set Goal: " + ouncesToDrink + " oz");
        optimalGoal = ouncesToDrink;
        return ouncesToDrink;
    }

    public LiveData<String> getText() {
        return mText;
    }
}