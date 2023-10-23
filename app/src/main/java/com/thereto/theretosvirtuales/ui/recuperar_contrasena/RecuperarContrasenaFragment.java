package com.thereto.theretosvirtuales.ui.recuperar_contrasena;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.thereto.theretosvirtuales.databinding.FragmentRecuperarContrasenaBinding;

public class RecuperarContrasenaFragment extends Fragment {

    private FragmentRecuperarContrasenaBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentRecuperarContrasenaBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}