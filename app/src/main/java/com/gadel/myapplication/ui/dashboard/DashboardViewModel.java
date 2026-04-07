package com.gadel.myapplication.ui.dashboard;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.gadel.myapplication.data.local.model.CategoryCount;
import com.gadel.myapplication.data.repository.ApprovalRepository;
import com.gadel.myapplication.utils.SessionManager;

import java.util.List;

public class DashboardViewModel extends AndroidViewModel {

    private final ApprovalRepository repository;
    private final SessionManager sessionManager;
    private final LiveData<List<CategoryCount>> categoryCountsLiveData;

    // ¡NUEVO! Variable para avisarle a la pantalla cuándo esconder la bolita de carga
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);

    public DashboardViewModel(@NonNull Application application) {
        super(application);
        repository = new ApprovalRepository(application);
        sessionManager = new SessionManager(application);

        categoryCountsLiveData = repository.getCategoryCounts();
    }

    public LiveData<List<CategoryCount>> getCategoryCounts() {
        return categoryCountsLiveData;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    // ¡NUEVO! Obtenemos el nombre del usuario logueado
    public String getUserName() {
        // Si tienes el nombre guardado en SessionManager, devuélvelo.
        // Si no lo tienes aún, devuelve un valor por defecto o el Token parcial.
        String name = sessionManager.getUsername();
        return (name != null && !name.isEmpty()) ? name : "Gerente";
    }

    public void forceSync() {
        // Mostramos la bolita de carga
        isLoading.setValue(true);

        // 1. Enviamos lo pendiente
        repository.syncOutboxToServer();

        // 2. Descargamos lo nuevo
        repository.syncPurchaseRequisitions();

        // Ocultamos la bolita de carga después de un pequeño retraso
        // En un entorno real, el repositorio debería avisarnos cuando termine la llamada Retrofit
        new android.os.Handler().postDelayed(() -> {
            isLoading.setValue(false);
        }, 1500); // 1.5 segundos simulados para la animación
    }

    public LiveData<Boolean> getTokenExpiredEvent() {
        return repository.getTokenExpiredError();
    }

    public void logout() {
        sessionManager.clearSession();
    }
}