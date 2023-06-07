package com.denisgruiax.blockchaintradingbot.application.mainactivity.charts;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ChartsViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public ChartsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("CHARTS");
    }

    public LiveData<String> getText() {
        return mText;
    }
}