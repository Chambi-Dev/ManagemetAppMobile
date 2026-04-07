package com.gadel.myapplication.data.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.gadel.myapplication.data.remote.api.ErpApiService;
import com.gadel.myapplication.data.remote.api.RetrofitClient;
import com.gadel.myapplication.data.remote.dto.AuthDTOs;
import com.gadel.myapplication.data.remote.dto.JwtPayloadDTO;
import com.gadel.myapplication.utils.JwtUtils;
import com.gadel.myapplication.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthRepository {

    private ErpApiService apiService;
    private SessionManager sessionManager;

    public AuthRepository(Context context) {
        apiService = RetrofitClient.getInstance().getApi();
        sessionManager = new SessionManager(context);
    }

    // Método para verificar si el usuario ya está logueado al abrir la app
    public boolean isUserLoggedIn() {
        return sessionManager.getAuthToken() != null;
    }

    // PASO 1: Intentar Login Inicial
    // Retornamos un LiveData con el Payload decodificado para que la pantalla dibuje los roles
    public LiveData<JwtPayloadDTO> attemptPreAuth(String email, String password) {
        MutableLiveData<JwtPayloadDTO> resultData = new MutableLiveData<>();

        AuthDTOs.LoginRequest request = new AuthDTOs.LoginRequest(email, password);

        apiService.login(request).enqueue(new Callback<AuthDTOs.PreAuthResponse>() {
            @Override
            public void onResponse(Call<AuthDTOs.PreAuthResponse> call, Response<AuthDTOs.PreAuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // 1. Recibimos el preAuthToken
                    String preToken = response.body().preAuthToken;
                    // 2. Lo decodificamos localmente (sin internet)
                    JwtPayloadDTO payload = JwtUtils.decodeToken(preToken);
                    //   guardar temporalmente el preToken en memoria aquí si lo necesitas
                    if (payload != null) {
                        payload.token = preToken; // Guardamos el sobre dentro de la carta
                    }

                    // 4. Se lo enviamos a la pantalla
                    resultData.setValue(payload);
                } else {
                    Log.e("ERROR_API", "El servidor rechazó la petición. Código: " + response.code());                    resultData.setValue(null); // Indica error de credenciales
                }
            }

            @Override
            public void onFailure(Call<AuthDTOs.PreAuthResponse> call, Throwable t) {

                Log.e("ERROR_RED", "No se pudo conectar a Spring Boot: " + t.getMessage());
                resultData.setValue(null); // Indica error de red
            }
        });

        return resultData;
    }

    // PASO 2: Enviar el rol seleccionado y obtener el pase final
    public LiveData<Boolean> confirmRoleSelection(Integer userId, Integer roleId, String preAuthToken) {
        MutableLiveData<Boolean> isSuccess = new MutableLiveData<>();

        AuthDTOs.RoleSelectionRequest request = new AuthDTOs.RoleSelectionRequest(userId, roleId, preAuthToken);

        apiService.selectRole(request).enqueue(new Callback<AuthDTOs.FinalAuthResponse>() {
            @Override
            public void onResponse(Call<AuthDTOs.FinalAuthResponse> call, Response<AuthDTOs.FinalAuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // ¡Éxito! Guardamos el token final en la bóveda segura
                    sessionManager.saveAuthToken(response.body().token);
                    isSuccess.setValue(true);
                } else {
                    isSuccess.setValue(false);
                }
            }

            @Override
            public void onFailure(Call<AuthDTOs.FinalAuthResponse> call, Throwable t) {
                isSuccess.setValue(false);
            }
        });

        return isSuccess;
    }
}
