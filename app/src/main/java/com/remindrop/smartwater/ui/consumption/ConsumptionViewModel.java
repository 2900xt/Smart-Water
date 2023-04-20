package com.remindrop.smartwater.ui.consumption;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ConsumptionViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public ConsumptionViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is notifications fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}