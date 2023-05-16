package com.denisgruiax.blockchaintradingbot.ui.walletstatistics;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import multiversx.Address;

public class WalletStatisticsModel extends ViewModel {

    private final MutableLiveData<String> balance;
    private final MutableLiveData<String> dailyPNL;

    private Address address;

    public WalletStatisticsModel() {
        balance = new MutableLiveData<>();
        dailyPNL = new MutableLiveData<>();

        MediatorLiveData<String> mediatorLiveData = new MediatorLiveData<>();
        mediatorLiveData.addSource(balance, text -> mediatorLiveData.setValue(text));
        mediatorLiveData.addSource(dailyPNL, text -> mediatorLiveData.setValue(text));

    }

    public LiveData<String> getText() {
        return balance;
    }
}