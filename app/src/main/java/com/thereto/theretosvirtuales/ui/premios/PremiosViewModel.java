package com.thereto.theretosvirtuales.ui.premios;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PremiosViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public PremiosViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}