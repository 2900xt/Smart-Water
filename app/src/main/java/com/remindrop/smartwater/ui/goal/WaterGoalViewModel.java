package com.remindrop.smartwater.ui.goal;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class WaterGoalViewModel extends ViewModel {

    private final MutableLiveData<String> mText;
    private static final double EXCERCIZE_FACTOR = 12.0 / 30.0;
    private static final double WEIGHT_FACTOR = 2.0 / 3.0;


    public WaterGoalViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("=  0.0 oz");
    }

    public double calculateOptimalGoal(double weight, double minExcercise)
    {
        double ouncesToDrink =  (weight * WEIGHT_FACTOR) + (minExcercise * EXCERCIZE_FACTOR);
        mText.setValue("= " + ouncesToDrink + " oz");
        return ouncesToDrink;
    }

    public LiveData<String> getText() {
        return mText;
    }
}