package com.theretos.helpers;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateHelpers {
    public static String dateNow() {
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date fechaActual = new Date();
        return formato.format(fechaActual);
    }
}
