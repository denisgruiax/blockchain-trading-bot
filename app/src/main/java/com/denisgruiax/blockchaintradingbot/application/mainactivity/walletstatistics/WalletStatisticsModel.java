package com.denisgruiax.blockchaintradingbot.application.mainactivity.walletstatistics;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class WalletStatisticsModel extends ViewModel {

    private final MutableLiveData<String> totalBalance;

    private final MutableLiveData<String> price2;
    private final MutableLiveData<String> price3;
    private final MutableLiveData<String> price4;
    private final MutableLiveData<String> price5;
    private final MutableLiveData<String> price6;

    public WalletStatisticsModel() {
        totalBalance = new MutableLiveData<>();

        price2 = new MutableLiveData<>();
        price3 = new MutableLiveData<>();
        price4 = new MutableLiveData<>();
        price5 = new MutableLiveData<>();
        price6 = new MutableLiveData<>();

        MediatorLiveData<String> mediatorLiveData = new MediatorLiveData<>();
        mediatorLiveData.addSource(totalBalance, text -> mediatorLiveData.setValue(text));

        mediatorLiveData.addSource(price2, text -> mediatorLiveData.setValue(text));
        mediatorLiveData.addSource(price3, text -> mediatorLiveData.setValue(text));
        mediatorLiveData.addSource(price4, text -> mediatorLiveData.setValue(text));
        mediatorLiveData.addSource(price5, text -> mediatorLiveData.setValue(text));
        mediatorLiveData.addSource(price6, text -> mediatorLiveData.setValue(text));
    }

    public LiveData<String> getTotalBalanceText() {
        return totalBalance;
    }

    public LiveData<String> getPrice2() {
        return price2;
    }

    public LiveData<String> getPrice3() {
        return price3;
    }

    public LiveData<String> getPrice4() {
        return price4;
    }

    public LiveData<String> getPrice5() {
        return price5;
    }

    public LiveData<String> getPrice6() {
        return price6;
    }
}