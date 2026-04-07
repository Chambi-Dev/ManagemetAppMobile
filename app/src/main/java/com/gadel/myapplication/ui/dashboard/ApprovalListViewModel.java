package com.gadel.myapplication.ui.dashboard;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.gadel.myapplication.data.local.entity.ApprovalTask;
import com.gadel.myapplication.data.repository.ApprovalRepository;

import java.util.List;

public class ApprovalListViewModel extends AndroidViewModel {

    private final ApprovalRepository repository;

    public ApprovalListViewModel(@NonNull Application application) {
        super(application);
        repository = new ApprovalRepository(application);
    }

    // Le pedimos al Repositorio la lista filtrada por el nombre de la categoría
    public LiveData<List<ApprovalTask>> getTasksByCategory(String categoryName) {
        return repository.getTasksByCategory(categoryName);
    }
}