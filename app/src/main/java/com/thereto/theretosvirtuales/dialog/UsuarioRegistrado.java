package com.thereto.theretosvirtuales.dialog;
import android.app.AlertDialog;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.thereto.theretosvirtuales.R;
import com.thereto.theretosvirtuales.interfas.DialogCallback;

public class UsuarioRegistrado {
    private final Context context;
    private final DialogCallback dialogCallback;
    AlertDialog dialog;

    public UsuarioRegistrado(Context context, DialogCallback dialogCallback) {
        this.context = context;
        this.dialogCallback = dialogCallback;
    }


    public void show() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_usuario_registrado, null);

        TextView messageTextView = dialogView.findViewById(R.id.messageTextView);
        Button positiveButton = dialogView.findViewById(R.id.positiveButton);

        String mensaje = "Has completado tu registro en <font color='#FF0000'>TheRetos</font>, se te ha enviado un correo de verificacion al correo con el que te registraste.<br><br>Si no lo llegas a ver, busca en la carpeta de <strong>span</strong>.<br><br>Es necesario que valides tu correo para el inicio en <font color='#FF0000'>TheRetos</font>";
        messageTextView.setText(Html.fromHtml(mensaje, Html.FROM_HTML_MODE_LEGACY));
        // Configura los clics de los botones
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
