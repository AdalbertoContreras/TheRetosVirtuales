package com.thereto.theretosvirtuales.dialog;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.thereto.theretosvirtuales.R;
import com.thereto.theretosvirtuales.interfas.DialogCallback;

public class MensajeBienvenida {
    private final Context context;
    private final DialogCallback dialogCallback;
    AlertDialog dialog;

    public MensajeBienvenida(Context context, DialogCallback dialogCallback) {
        this.context = context;
        this.dialogCallback = dialogCallback;
    }


    public void show() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_mensaje_de_bienvenida, null);

        Button positiveButton = dialogView.findViewById(R.id.positiveButton);
        positiveButton.setOnClickListener(view -> {
            if (dialogCallback != null) {
                dialog.dismiss();
                dialogCallback.onPositiveButtonClick();
            }
        });

        builder.setView(dialogView);
        dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();
    }
}
