package com.thereto.theretosvirtuales.ui.recuperar_contrasena;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RecuperarContrasenaViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public RecuperarContrasenaViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is slideshow fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}