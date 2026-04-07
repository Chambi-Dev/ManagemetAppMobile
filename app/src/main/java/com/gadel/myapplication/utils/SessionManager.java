package com.gadel.myapplication.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    // Nombres de los archivos y llaves de seguridad
    private static final String PREF_NAME = "ErpSessionPref";
    private static final String KEY_TOKEN = "AUTH_TOKEN";
    private static final String KEY_USERNAME = "USERNAME";

    // Variables globales de la clase (¡Esto soluciona tu error rojo!)
    private final SharedPreferences prefs;
    private final SharedPreferences.Editor editor;

    // Constructor: Inicializa la bóveda cuando lo llamamos desde cualquier Activity/ViewModel
    public SessionManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    // =========================================================
    // GUARDAR DATOS (Esto lo usarás en tu LoginActivity)
    // =========================================================

    public void saveAuthToken(String token) {
        editor.putString(KEY_TOKEN, token);
        editor.apply(); // apply() es asíncrono (no congela la pantalla)
    }

    public void saveUsername(String username) {
        editor.putString(KEY_USERNAME, username);
        editor.apply();
    }

    // =========================================================
    // LEER DATOS (Esto lo usan tus Repositorios y ViewModels)
    // =========================================================

    public String getAuthToken() {
        return prefs.getString(KEY_TOKEN, null); // Si no hay token, devuelve null
    }

    public String getUsername() {
        // Busca el nombre. Si no lo encuentra (ej. sesión antigua), devuelve "Gerente"
        return prefs.getString(KEY_USERNAME, "Gerente");
    }

    // =========================================================
    // DESTRUIR DATOS (Logout)
    // =========================================================

    public void clearSession() {
        editor.clear(); // Borra absolutamente todo de la bóveda
        editor.apply();
    }
}