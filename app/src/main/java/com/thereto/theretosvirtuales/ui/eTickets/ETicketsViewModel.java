package com.thereto.theretosvirtuales.ui.eTickets;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ETicketsViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public ETicketsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is slideshow fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}