package com.gadel.myapplication.data.local.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(tableName = "sync_actions")
public class SyncAction {

    @PrimaryKey(autoGenerate = true)
    public int id; // ID interno de la cola

    @ColumnInfo(name = "task_id")
    public String taskId; // Ej. MR310000024

    @ColumnInfo(name = "action_type")
    public String actionType; // "APPROVED" o "REJECTED"

    @ColumnInfo(name = "comments")
    public String comments; // El motivo del rechazo (si lo hay)

    @ColumnInfo(name = "created_at")
    public long createdAt; // Para saber en qué orden enviarlos al servidor
}