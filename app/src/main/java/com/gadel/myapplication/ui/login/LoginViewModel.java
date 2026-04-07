package com.gadel.myapplication.ui.login;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.gadel.myapplication.data.remote.dto.JwtPayloadDTO;
import com.gadel.myapplication.data.repository.AuthRepository;

public class LoginViewModel extends AndroidViewModel {

    private AuthRepository authRepository;

    // --- VARIABLES DE ESTADO VISUAL ---
    // Usamos MutableLiveData para que la Interfaz reaccione automáticamente si estos valores cambian
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public LoginViewModel(@NonNull Application application) {
        super(application);
        // Inicializamos nuestro "cerebro" de datos enviándole el contexto de la app
        authRepository = new AuthRepository(application);
    }

    // Getters para que la Activity pueda "observar" estas variables
    public LiveData<Boolean> getIsLoading() { return isLoading; }
    public LiveData<String> getErrorMessage() { return errorMessage; }

    // --- LÓGICA DE NEGOCIO ---

    // Método para saber si debemos saltarnos el Login porque ya guardó su token antes
    public boolean isAlreadyLoggedIn() {
        return authRepository.isUserLoggedIn();
    }

    // PASO 1: Intentar el primer Login
    public LiveData<JwtPayloadDTO> attemptLogin(String username, String password) {
        // Validación básica (Regla de negocio: no enviar campos vacíos al servidor)
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            errorMessage.setValue("Por favor, ingresa tu usuario y contraseña.");
            return new MutableLiveData<>(); // Retornamos un dato vacío para no hacer la petición
        }

        // Encendemos el círculo de carga en la pantalla
        isLoading.setValue(true);

        // Le pasamos la pelota al Repositorio para que hable con Retrofit
        return authRepository.attemptPreAuth(username, password);
    }

    // PASO 2: Confirmar la selección del Rol
    public LiveData<Boolean> confirmRole(Integer userId, Integer roleId, String preAuthToken) {
        isLoading.setValue(true); // Volvemos a encender la carga
        return authRepository.confirmRoleSelection(userId, roleId, preAuthToken);
    }

    // Métodos auxiliares para que la Activity controle los estados de error y carga
    public void hideLoading() {
        isLoading.setValue(false);
    }

    public void showError(String message) {
        errorMessage.setValue(message);
    }
}