package com.denisgruiax.blockchaintradingbot.ui.walletstatistics;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import multiversx.Address;

public class WalletStatisticsModel extends ViewModel {

    private final MutableLiveData<String> balance;
    private final MutableLiveData<String> dailyPNL;
    private static Integer count;

    public WalletStatisticsModel() {
        balance = new MutableLiveData<>();
        balance.setValue("$30.43");

        dailyPNL = new MutableLiveData<>();
        dailyPNL.setValue("+12%");

        count = 0;

        MediatorLiveData<String> mediatorLiveData = new MediatorLiveData<>();

        mediatorLiveData.addSource(balance, text -> mediatorLiveData.setValue(text));
        mediatorLiveData.addSource(dailyPNL, text -> mediatorLiveData.setValue(text));
    }

    public LiveData<String> getBalance() {
        return balance;
    }
    
    public LiveData<String> getDailyPNL(){
        return dailyPNL;
    }

    public void setBalance(Integer value){
        balance.setValue(value.toString());
    }
    public void setB(){
        count++;
    }

    public Integer getB(){
        return count;
    }
}