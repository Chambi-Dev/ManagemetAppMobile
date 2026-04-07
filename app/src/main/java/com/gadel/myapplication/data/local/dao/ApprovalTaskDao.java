package com.gadel.myapplication.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.gadel.myapplication.data.local.entity.ApprovalTask;
import com.gadel.myapplication.data.local.model.CategoryCount;

import java.util.List;

@Dao
public interface ApprovalTaskDao {

    @Query("SELECT modulo_type AS categoryName, COUNT(task_id) AS pendingCount " +
            "FROM approval_tasks " +
            "WHERE local_status IS NULL OR local_status = 'PENDING' " +
            "GROUP BY modulo_type")
    LiveData<List<CategoryCount>> getCategoryCounts();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTasks(List<ApprovalTask> tasks);

    @Query("DELETE FROM approval_tasks")
    void deleteAllTasks();

    @Query("SELECT * FROM approval_tasks " +
            "WHERE modulo_type = :categoryName " +
            "AND (local_status IS NULL OR local_status = 'PENDING')")
    LiveData<List<ApprovalTask>> getTasksByCategory(String categoryName);

    @Query("UPDATE approval_tasks SET local_status = :newStatus WHERE task_id = :taskId")
    void updateTaskStatus(String taskId, String newStatus);

    // =========================================================
    // ¡NUEVO! Obtiene el ID numérico (Ej. 18) de Spring Boot
    // =========================================================
    @Query("SELECT backend_doc_id FROM approval_tasks WHERE task_id = :taskId LIMIT 1")
    Long getBackendIdByTaskId(String taskId);
}