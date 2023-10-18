package com.thereto.theretosvirtuales.ui.como_jugar;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ComoJugarViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public ComoJugarViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is slideshow fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}