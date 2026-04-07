package com.gadel.myapplication.ui.dashboard;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.gadel.myapplication.data.local.entity.ApprovalDetail;
import com.gadel.myapplication.data.repository.ApprovalRepository;

import java.util.List;

public class ApprovalDetailViewModel extends AndroidViewModel {

    private final ApprovalRepository repository;

    public ApprovalDetailViewModel(@NonNull Application application) {
        super(application);
        repository = new ApprovalRepository(application);
    }

    public LiveData<List<ApprovalDetail>> getDetails(String taskId) {
        return repository.getDetailsByTaskId(taskId);
    }

    // ¡ACTUALIZADO! Si es aprobar, no hay comentario, pasamos null o vacío
    public void approveTask(String taskId) {
        repository.updateTaskStatusLocal(taskId, "APPROVED", "");
    }

    // ¡ACTUALIZADO! Si es rechazar, pasamos el comentario que escribió el gerente
    public void rejectTask(String taskId, String comment) {
        repository.updateTaskStatusLocal(taskId, "REJECTED", comment);
    }
}