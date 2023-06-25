package com.denisgruiax.blockchaintradingbot.application.mainactivity.botstrategy;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class BotStrategyModel extends ViewModel {
    private final MutableLiveData<String> textViewCreateAndConfigureBot;

    private final MutableLiveData<String> textViewBotName;
    private final MutableLiveData<String> editTextSetBotName;

    private final MutableLiveData<String> textViewBehavior;
    private final MutableLiveData<List<String>> spinnerSelectBotBehavior;

    private final MutableLiveData<String> textViewStrategy;
    private final MutableLiveData<List<String>> spinnerSelectStrategy;

    private final MutableLiveData<String> textViewCrypto;
    private final MutableLiveData<List<String>> spinnerSelectCrypto;

    private final MutableLiveData<String> textViewLaunchBot;

    private final MutableLiveData<String> textViewBotNameForLaunch;
    private final MutableLiveData<List<String>> spinnerSelectBotForLaunch;

    public BotStrategyModel() {
        textViewCreateAndConfigureBot = new MutableLiveData<>();

        textViewBotName = new MutableLiveData<>();
        editTextSetBotName = new MutableLiveData<>();

        textViewBehavior = new MutableLiveData<>();
        spinnerSelectBotBehavior = new MutableLiveData<>();

        textViewStrategy = new MutableLiveData<>();
        spinnerSelectStrategy = new MutableLiveData<>();

        textViewCrypto = new MutableLiveData<>();
        spinnerSelectCrypto = new MutableLiveData<>();

        textViewLaunchBot = new MutableLiveData<>();

        textViewBotNameForLaunch = new MutableLiveData<>();
        spinnerSelectBotForLaunch = new MutableLiveData<>();

        MediatorLiveData<String> mediatorLiveData = new MediatorLiveData<>();

        mediatorLiveData.addSource(textViewCreateAndConfigureBot, mediatorLiveData::setValue);

        mediatorLiveData.addSource(textViewBotName, mediatorLiveData::setValue);
        mediatorLiveData.addSource(editTextSetBotName, mediatorLiveData::setValue);

        mediatorLiveData.addSource(textViewBehavior, mediatorLiveData::setValue);

        mediatorLiveData.addSource(textViewStrategy, mediatorLiveData::setValue);

        mediatorLiveData.addSource(textViewCrypto, mediatorLiveData::setValue);

        mediatorLiveData.addSource(textViewLaunchBot, mediatorLiveData::setValue);

        mediatorLiveData.addSource(textViewBotNameForLaunch, mediatorLiveData::setValue);

        textViewCreateAndConfigureBot.setValue("Create and configure the trading bot. ⚙️");
        textViewLaunchBot.setValue("Launch bot. 🚀");
    }

    public LiveData<String> getTextViewCreateAndConfigureBot() {
        return textViewCreateAndConfigureBot;
    }

    public LiveData<String> getTextViewBotName() {
        return textViewBotName;
    }

    public LiveData<String> getEditTextSetBotName() {
        return editTextSetBotName;
    }

    public LiveData<String> getTextViewBehavior() {
        return textViewBehavior;
    }

    public LiveData<List<String>> getSpinnerSelectBotBehavior() {
        return spinnerSelectBotBehavior;
    }

    public LiveData<String> getTextViewStrategy() {
        return textViewStrategy;
    }

    public LiveData<List<String>> getSpinnerSelectStrategy() {
        return spinnerSelectStrategy;
    }

    public LiveData<String> getTextViewCrypto() {
        return textViewCrypto;
    }
    public LiveData<List<String>> getSpinnerSelectCrypto() {
        return spinnerSelectCrypto;
    }

    public LiveData<String> getTextViewLaunchBot() {
        return textViewLaunchBot;
    }

    public LiveData<String> getTextViewBotNameForLaunch() {
        return textViewBotNameForLaunch;
    }

    public LiveData<List<String>> getSpinnerSelectBotForLaunch() {
        return spinnerSelectBotForLaunch;
    }
}