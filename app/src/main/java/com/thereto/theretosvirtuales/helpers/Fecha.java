package com.thereto.theretosvirtuales.helpers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Fecha {
    public static String formatearFecha(String fecha) {
        try {
            // Paso 1: Analizar la fecha en formato "yyyy-MM-dd" a un objeto Date
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            Date date = inputFormat.parse(fecha);

            // Paso 2: Formatear el Date en "mes/día" con el nombre del mes en español
            SimpleDateFormat outputFormat = new SimpleDateFormat("MMMM/d", new Locale("es", "ES"));
            // Obtener la fecha formateada
            String fechaFormateada = outputFormat.format(date);

            // Asegurarse de que la primera letra del mes esté en mayúscula
            String[] partes = fechaFormateada.split("/");
            if (partes.length == 2) {
                String mes = partes[0].substring(0, 1).toUpperCase() + partes[0].substring(1, 3);
                String dia = partes[1];
                if (dia.length() == 1) {
                    dia = "0" + dia;
                }
                return mes + "/" + dia;
            } else {
                return fechaFormateada;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return ""; // Manejo de errores si la fecha no se puede analizar.
        }
    }
}
