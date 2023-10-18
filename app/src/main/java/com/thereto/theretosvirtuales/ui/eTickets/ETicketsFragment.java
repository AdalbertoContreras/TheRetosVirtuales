package com.thereto.theretosvirtuales.ui.eTickets;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.thereto.theretosvirtuales.databinding.FragmentETicketsBinding;
import com.thereto.theretosvirtuales.databinding.FragmentQuienesSomosBinding;

public class ETicketsFragment extends Fragment {

    private FragmentETicketsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentETicketsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}