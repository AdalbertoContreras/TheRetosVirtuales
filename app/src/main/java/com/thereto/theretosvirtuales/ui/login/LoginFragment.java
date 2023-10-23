package com.thereto.theretosvirtuales.ui.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.thereto.theretosvirtuales.R;
import com.thereto.theretosvirtuales.databinding.FragmentLoginBinding;
import com.thereto.theretosvirtuales.ui.recuperar_contrasena.RecuperarContrasenaFragment;
import com.thereto.theretosvirtuales.ui.registrar.RegistrarFragment;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentLoginBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        binding.crearCuentaButton.setOnClickListener(v -> {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment_content_home, new RegistrarFragment())
                    .addToBackStack(null)
                    .commit();
        });
        binding.contrasenaOlvidadaTextView.setOnClickListener( v -> {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment_content_home, new RecuperarContrasenaFragment())
                    .addToBackStack(null)
                    .commit();
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}