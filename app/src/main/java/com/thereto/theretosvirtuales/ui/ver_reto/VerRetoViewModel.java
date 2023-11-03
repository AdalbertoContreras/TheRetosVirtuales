package com.thereto.theretosvirtuales.ui.ver_reto;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class VerRetoViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public VerRetoViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}