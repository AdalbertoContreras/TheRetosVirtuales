package com.thereto.theretosvirtuales.ui.registrar;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.thereto.model.Player;
import com.thereto.theretosvirtuales.R;
import com.thereto.theretosvirtuales.databinding.FragmentLoginBinding;
import com.thereto.theretosvirtuales.databinding.FragmentRegistroBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.thereto.theretosvirtuales.dialog.DialogCallback;
import com.thereto.theretosvirtuales.dialog.UsuarioRegistrado;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class RegistrarFragment extends Fragment {

    private FragmentRegistroBinding binding;
    private int documentoSelecionado = 1;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRegistroBinding.inflate(inflater, container, false);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.tipos_de_documento, // Define los tipos de documento en tu archivo de recursos strings.xml
                android.R.layout.simple_spinner_item
        );
        binding.fechaNacimientoEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
        // Especifica el diseño de los elementos desplegados
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.documentoSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Obtener el índice de la opción seleccionada
                int tipoDocumentoSeleccionado = position;

                // Realizar acciones basadas en la opción seleccionada
                switch (tipoDocumentoSeleccionado) {
                    case 0: // Cédula
                        documentoSelecionado = 1;
                        break;
                    case 1: // Tarjeta de Identidad
                        documentoSelecionado = 2;
                        break;
                    case 2: // Pasaporte
                        documentoSelecionado = 3;
                        break;
                    default:
                        // Otra acción por defecto
                        documentoSelecionado = -1;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Método llamado cuando no hay nada seleccionado
            }
        });
        // Establece el ArrayAdapter en el Spinner
        binding.documentoSpinner.setAdapter(adapter);
        View root = binding.getRoot();
        binding.crearCuentaButton.setOnClickListener(v -> {
            /**
             * Validar datos de usuario
             */
            if (!datosDeUsuarioValidos()) {
                return;
            }
            /**
             * Verifica si el email ingresado ya existe en el sistema
             */
            validarSiExisteEmail(task -> {
                if (task.isSuccessful()) {
                    SignInMethodQueryResult result = task.getResult();
                    List<String> signInMethods = result.getSignInMethods();

                    if (signInMethods != null && !signInMethods.isEmpty()) {
                        Toast.makeText(requireContext(), "Ya existe un usuario registrado con este correo.", Toast.LENGTH_SHORT).show();
                    } else {
                        registrarUsuario();
                    }
                } else {
                    // Hubo un error en la consulta.
                }
            });


        });
        return root;
    }

    public void registrarUsuario() {
        Player newPlayer = llenarDatosDeUsuario();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collectionReferencePlayer = db.collection("player");
        CollectionReference collectionReferenceUsers= db.collection("users");
        mAuth.createUserWithEmailAndPassword(newPlayer.email, binding.contrasenaEditText.getText().toString())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Registro exitoso, ahora puedes guardar los datos adicionales en Firestore
                        FirebaseUser user = mAuth.getCurrentUser();
                        String userId = user.getUid();

                        Map<String, Object> usuario = new HashMap<>();
                        usuario.put("displayName", newPlayer.nombres);
                        usuario.put("email", newPlayer.email);
                        usuario.put("emailVerified", false);
                        usuario.put("uid", userId);


                        newPlayer.uid = userId;

                        collectionReferenceUsers.document(userId).set(usuario)
                                .addOnSuccessListener(aVoid -> {
                                    // Éxito
                                    Log.d("Firestore", "Documento creado con éxito");
                                })
                                .addOnFailureListener(e -> {
                                    // Error
                                    Log.w("Firestore", "Error al crear documento", e);
                                });
                        collectionReferencePlayer.document(userId).set(newPlayer)
                                .addOnSuccessListener(aVoid -> {
                                    mAuth.signOut();
                                    // Éxito
                                    user.sendEmailVerification().addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            new UsuarioRegistrado(getContext(), new DialogCallback() {
                                                @Override
                                                public void onPositiveButtonClick() {
                                                    new UsuarioRegistrado(getContext(), new DialogCallback() {
                                                        @Override
                                                        public void onPositiveButtonClick() {
                                                            requireActivity().onBackPressed();
                                                        }

                                                        @Override
                                                        public void onNegativeButtonClick() {
                                                            requireActivity().onBackPressed();
                                                        }
                                                    }).show();
                                                }

                                                @Override
                                                public void onNegativeButtonClick() {

                                                }
                                            }).show();

                                        } else {
                                            Toast.makeText(requireContext(), "Usuario registrado en el sistema.", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                })
                                .addOnFailureListener(e -> {
                                    // Error
                                    Log.w("Firestore", "Error al crear documento", e);
                                });
                    } else {
                        Toast.makeText(requireContext(), "Este correo ya se encuentra registrado en TheRetos.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void validarSiExisteEmail(OnCompleteListener<SignInMethodQueryResult> resultado) {
        FirebaseAuth.getInstance().fetchSignInMethodsForEmail("correo@ejemplo.com")
                .addOnCompleteListener(resultado);
    }

    public Player llenarDatosDeUsuario() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hora = calendar.get(Calendar.HOUR_OF_DAY);
        int minuto = calendar.get(Calendar.MINUTE);
        int segundo = calendar.get(Calendar.SECOND);
        Player newPlayer = new Player();
        newPlayer.cellNumber = binding.numeroCelularEditText.getText().toString();
        newPlayer.documentNumber = binding.numeroCelularEditText.getText().toString();
        newPlayer.nombres = binding.nombresEditText.getText().toString();
        newPlayer.documentType = documentoSelecionado;
        newPlayer.birthdate = binding.fechaNacimientoEditText.getText().toString();
        newPlayer.countryOfPlay = 1;
        newPlayer.dataTreatmentPolicy = true;
        newPlayer.direccion = "";
        newPlayer.displayName = "";
        newPlayer.email = binding.correoElectronicoEditText.getText().toString();
        newPlayer.isOfLegalAge = true;
        newPlayer.nickname = "";
        newPlayer.patchProfilePicture = "";
        newPlayer.receiveAdvertising = false;
        newPlayer.registrationDate = year + "/" + (month + 1) + "/" + day;
        newPlayer.registrationTime = hora + ":" + minuto + ":" + segundo;
        newPlayer.sourceOfMyMoney = true;
        newPlayer.surname = binding.numeroCelularEditText.getText().toString();
        return newPlayer;
    }

    public boolean datosDeUsuarioValidos() {
        if (documentoSelecionado == -1) {
            Toast.makeText(requireContext(), "Selecione su tipo de documento", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.numeroDocumentoEditText.getText().toString().isEmpty()) {
            Toast.makeText(requireContext(), "Selecione su tipo de documento", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.nombresEditText.getText().toString().isEmpty()) {
            Toast.makeText(requireContext(), "Ingrese sus nombres y apoellidos", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.correoElectronicoEditText.getText().toString().isEmpty()) {
            Toast.makeText(requireContext(), "Ingrese su correo electronico", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.contrasenaEditText.getText().toString().isEmpty()) {
            Toast.makeText(requireContext(), "Ingrese su contraseña", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.contrasenaEditText.getText().toString().length() < 8) {
            Toast.makeText(requireContext(), "La contraseña debe tener minimo 8 caracteres.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!binding.verificarContrasenaEditTest.getText().toString().equals(binding.contrasenaEditText.getText().toString())) {
            Toast.makeText(requireContext(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.numeroCelularEditText.getText().toString().isEmpty()) {
            Toast.makeText(requireContext(), "Ingrese su numero de celular", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!binding.estoyDeAcuerdoCheckBox.isChecked()) {
            Toast.makeText(requireContext(), "Acepte las codiciones para el registro.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!binding.certificoCheckBox.isChecked()) {
            Toast.makeText(requireContext(), "Acepte las codiciones para el registro.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!binding.autorizoCeckBox.isChecked()) {
            Toast.makeText(requireContext(), "Acepte las codiciones para el registro.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void showDatePickerDialog() {
        // Obtener la fecha actual
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Crear un DatePickerDialog con una fecha máxima (hoy)
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // La fecha seleccionada se recibe aquí
                        String selectedDate = year + "/" + (month + 1) + "/" + dayOfMonth;
                        binding.fechaNacimientoEditText.setText(selectedDate);
                        // Realizar acciones con la fecha seleccionada
                    }
                },
                year, month, day
        );

        // Establecer la fecha máxima para limitar la selección
        datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());

        // Mostrar el diálogo
        datePickerDialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}