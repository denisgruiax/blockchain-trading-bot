package com.denisgruiax.blockchaintradingbot.application.mainactivity.others;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class OthersViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public OthersViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is slideshow fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}