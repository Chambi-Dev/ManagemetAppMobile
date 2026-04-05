package com.gadel.myapplication.data.local.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.gadel.myapplication.data.local.entity.SyncAction;

import java.util.List;

@Dao
public interface SyncActionDao {

    // 1. Guardar la decisión del usuario (Se usa cuando está sin internet)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSyncAction(SyncAction syncAction);

    // 2. Obtener la fila de trabajo pendiente (Lo usa el WorkManager al volver el internet)
    // IMPORTANTE: Ordenamos por 'timestamp' ASC para enviar a Spring Boot primero lo más antiguo
    @Query("SELECT * FROM sync_action WHERE sync_status = 'QUEUED' ORDER BY timestamp ASC")
    List<SyncAction> getPendingSyncActions();

    // 3. Actualizar el estado si falla la subida (ej. Si Spring Boot responde Error 500)
    @Query("UPDATE sync_action SET sync_status = :newStatus WHERE action_id = :actionId")
    void updateSyncStatus(Long actionId, String newStatus);

    // 4. Eliminar la tarea de la cola (Se usa cuando Spring Boot responde 200 OK)
    @Delete
    void deleteSyncAction(SyncAction syncAction);

    // 5. Consulta extra útil: Saber si hay cosas pendientes para mostrar un aviso en la UI
    @Query("SELECT COUNT(*) FROM sync_action WHERE sync_status = 'QUEUED'")
    int getPendingActionsCount();

}
