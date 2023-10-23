package com.thereto.theretosvirtuales.ui.registrar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.thereto.theretosvirtuales.databinding.FragmentLoginBinding;
import com.thereto.theretosvirtuales.databinding.FragmentRegistroBinding;

public class RegistrarFragment extends Fragment {

    private FragmentRegistroBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentRegistroBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}