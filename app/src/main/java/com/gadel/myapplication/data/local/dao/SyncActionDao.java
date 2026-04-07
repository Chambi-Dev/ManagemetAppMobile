package com.gadel.myapplication.data.local.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.gadel.myapplication.data.local.entity.SyncAction;

import java.util.List;

@Dao
public interface SyncActionDao {

    // Mete la acción a la cola
    @Insert
    void insert(SyncAction syncAction);

    // Busca todas las acciones que están haciendo fila (esperando internet)
    @Query("SELECT * FROM sync_actions ORDER BY created_at ASC")
    List<SyncAction> getAllPendingSyncs();

    // Borra la acción de la cola una vez que Spring Boot nos diga "Ok, recibido"
    @Delete
    void delete(SyncAction syncAction);
}