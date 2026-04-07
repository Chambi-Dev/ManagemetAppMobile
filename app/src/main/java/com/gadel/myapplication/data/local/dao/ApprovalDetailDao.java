package com.gadel.myapplication.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.gadel.myapplication.data.local.entity.ApprovalDetail;

import java.util.List;

@Dao
public interface ApprovalDetailDao {

    // Guardar una lista de detalles
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertDetails(List<ApprovalDetail> details);

    // Buscar todos los detalles que pertenezcan a una tarea en específico
    @Query("SELECT * FROM approval_details WHERE task_id = :taskId")
    LiveData<List<ApprovalDetail>> getDetailsByTaskId(String taskId);
}