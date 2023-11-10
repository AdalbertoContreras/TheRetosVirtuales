package com.thereto.theretosvirtuales.ui.login;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.thereto.theretosvirtuales.R;
import com.thereto.theretosvirtuales.databinding.FragmentLoginBinding;
import com.thereto.theretosvirtuales.interfas.OnTaskCompleteListener;
import com.thereto.theretosvirtuales.ui.recuperar_contrasena.RecuperarContrasenaFragment;
import com.thereto.theretosvirtuales.ui.registrar.RegistrarFragment;

public class LoginFragment extends Fragment {
    private FirebaseAuth mAuth;
    private FragmentLoginBinding binding;
    private OnTaskCompleteListener autenticarListener;

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
            RecuperarContrasenaFragment recuperarContrasenaFragment = new RecuperarContrasenaFragment();
            recuperarContrasenaFragment.setAutenticarListener(autenticarListener);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment_content_home, recuperarContrasenaFragment)
                    .addToBackStack(null)
                    .commit();
        });
        binding.iniciarSesionButton.setOnClickListener(v -> {
            autenticar();
        });
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.

    }

    public void autenticar() {
        if (binding.correoElectronicoEditText.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "Ingrese su correo electronico", Toast.LENGTH_SHORT).show();
            return;
        }
        if (binding.contrasenaEditText.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "Ingrese su contraseña", Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth.signInWithEmailAndPassword(binding.correoElectronicoEditText.getText().toString(), binding.contrasenaEditText.getText().toString())
        .addOnCompleteListener( getActivity(), task -> {
            if (task.isSuccessful()) {
                mAuth = FirebaseAuth.getInstance();
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if(currentUser == null){

                } else {
                   if (!currentUser.isEmailVerified()) {
                       FirebaseAuth.getInstance().signOut();
                       Toast.makeText(getContext(), "Este correo no se encuentra verificado.", Toast.LENGTH_SHORT).show();
                   } else {
                       // Sign in success, update UI with the signed-in user's information
                       Toast.makeText(getContext(), "Bienvenidos a TheRetos", Toast.LENGTH_SHORT).show();
                       if (autenticarListener != null) {
                           autenticarListener.onTaskComplete();
                       }
                   }
                }


            } else {
                // If sign in fails, display a message to the user.
                Log.w("login google", "signInWithEmail:failure", task.getException());
                Toast.makeText(getContext(), "Usuario o contraseña incorrecto.",
                        Toast.LENGTH_SHORT).show();
            }
        });
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