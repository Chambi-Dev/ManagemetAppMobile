package com.gadel.myapplication.utils;


import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.room.TypeConverter;
import java.time.LocalDateTime;

public class Converters {

    // Convierte el String de la base de datos a LocalDateTime para tu código Java
    @TypeConverter
    public static LocalDateTime fromString(String value) {
        return value == null ? null : LocalDateTime.parse(value);
    }

    // Convierte tu LocalDateTime a String para guardarlo en SQLite
    @TypeConverter
    public static String dateToString(LocalDateTime date) {
        return date == null ? null : date.toString();
    }
}
