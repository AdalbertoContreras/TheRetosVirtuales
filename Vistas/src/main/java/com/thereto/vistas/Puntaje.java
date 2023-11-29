package com.thereto.vistas;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * A simple {@link} subclass.
 * Use the {@link Puntaje} factory method to
 * create an instance of this fragment.
 */
public class Puntaje extends DialogFragment {

    private Long puntaje = 0l;

    // Interfaz para manejar eventos del diálogo
    public interface CustomDialogListener {
        void permisosLeidos();
    }

    // Declaración de una instancia del Listener
    private CustomDialogListener listener;

    // Método para inicializar la instancia del fragmento
    public static Puntaje init(CustomDialogListener evento, Long puntaje) {
        Puntaje dialog = new Puntaje();
        dialog.setCustomDialogListener(evento);
        dialog.setPuntaje(puntaje);
        return dialog;
    }

    // Método para establecer el Listener
    public void setCustomDialogListener(CustomDialogListener listener) {
        this.listener = listener;
    }

    // Método para establecer el puntaje
    public void setPuntaje(Long puntaje) {
        this.puntaje = puntaje;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.mensaje_puntaje, null);

        final TextView puntajeTextView = view.findViewById(R.id.puntajeTextView);
        if (puntaje != null) {

        puntajeTextView.setText(puntaje + " Puntos");
        }
        setCancelable(false);
        Button aceptarButton = view.findViewById(R.id.positiveButton);
        aceptarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.permisosLeidos();
                }
                dismiss();
            }
        });

        builder.setView(view);
        builder.setCancelable(false);

        return builder.create();
    }
}
