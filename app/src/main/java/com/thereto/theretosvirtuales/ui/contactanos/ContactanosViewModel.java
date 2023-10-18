package com.thereto.theretosvirtuales.ui.contactanos;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ContactanosViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public ContactanosViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is slideshow fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}