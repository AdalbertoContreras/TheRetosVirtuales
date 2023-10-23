package com.thereto.theretosvirtuales.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;

import com.thereto.theretosvirtuales.interfas.LogoutDialogListener;

public class LogoutConfirmationDialogFragment extends DialogFragment {

    private LogoutDialogListener listener;

    // Opcionalmente, puedes sobrescribir el método onAttach para asegurarte de que la actividad implemente la interfaz.
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (LogoutDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " debe implementar la interfaz LogoutDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Cerrar Sesión")
                .setMessage("¿Estás seguro de que deseas cerrar sesión?")
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (listener != null) {
                            listener.onLogoutConfirmed();
                        }
                        dismiss(); // Cierra el diálogo
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (listener != null) {
                            listener.onLogoutCancelled();
                        }
                        dialog.dismiss();
                    }
                });
        return builder.create();
    }

}
