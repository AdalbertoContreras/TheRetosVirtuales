package com.thereto.theretosvirtuales.ui.recuperar_contrasena;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.thereto.theretosvirtuales.databinding.FragmentRecuperarContrasenaBinding;
import com.thereto.theretosvirtuales.interfas.OnTaskCompleteListener;

public class RecuperarContrasenaFragment extends Fragment {

    private FragmentRecuperarContrasenaBinding binding;
    private OnTaskCompleteListener autenticarListener;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentRecuperarContrasenaBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        binding.recuperarContrasenaButton.setOnClickListener(v -> {
            if (binding.correoElectronicoEditText.getText().toString().isEmpty()) {
                Toast.makeText(getContext(), "Ingrese su correo electronico.", Toast.LENGTH_SHORT).show();
                return;
            }
            FirebaseAuth auth = FirebaseAuth.getInstance();
            auth.sendPasswordResetEmail(binding.correoElectronicoEditText.getText().toString())
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // El correo de restablecimiento de contraseña se ha enviado con éxito.
                            // Puedes mostrar un mensaje de confirmación al usuario.
                            Toast.makeText(getContext(), "Se ha enviado un correo de restablecimiento de contraseña.", Toast.LENGTH_SHORT).show();
                            if (autenticarListener != null) {
                                autenticarListener.onTaskComplete();
                            }
                        } else {
                            // Ha ocurrido un error. Puedes mostrar un mensaje de error al usuario.
                            Toast.makeText(getContext(), "No se pudo enviar el correo de restablecimiento de contraseña.", Toast.LENGTH_SHORT).show();
                        }
                    });
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void setAutenticarListener(OnTaskCompleteListener listener) {
        this.autenticarListener = listener;
    }
}