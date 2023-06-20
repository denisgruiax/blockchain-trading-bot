package com.denisgruiax.blockchaintradingbot.application.mainactivity.botstrategy;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BotStrategyModel extends ViewModel {
    private final MutableLiveData<String> textViewCreateAndConfigureBot;

    //private final MutableLiveData<String> textViewBotName;
    private final MutableLiveData<String> textViewSelectTradingBot;

    //private final MutableLiveData<String> textViewSelectTradingBot;
    //private final MutableLiveData<String> textViewSelectTradingBot;

    public BotStrategyModel() {
        textViewCreateAndConfigureBot = new MutableLiveData<>();
        textViewSelectTradingBot = new MutableLiveData<>();
    }

    public LiveData<String> getCreateAndConfigureBot() {
        return textViewCreateAndConfigureBot;
    }

    public LiveData<String> getSelectTradingBotText(){
        return textViewSelectTradingBot;
    }
}