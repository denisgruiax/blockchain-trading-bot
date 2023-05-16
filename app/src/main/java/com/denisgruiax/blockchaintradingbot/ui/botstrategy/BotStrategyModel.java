package com.denisgruiax.blockchaintradingbot.ui.botstrategy;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BotStrategyModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public BotStrategyModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is a fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}