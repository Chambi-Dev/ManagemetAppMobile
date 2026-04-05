package com.gadel.myapplication.data.local.entity;

import android.nfc.tech.NfcA;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Entity(tableName = "sync_action")
@Accessors(chain = true)
public class SyncAction {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "action_id")
    private String actionId;

    @ColumnInfo(name = "task_id")
    private String taskId;

    @ColumnInfo(name = "action_type")
    private String actionType; // e.g., "APPROVE", "REJECT", "COMMENT"

    @ColumnInfo(name = "reason_id")
    private String reasonId; // Optional, for rejection reasons

    @ColumnInfo(name = "comment")
    private String comment; // Optional, for any comments

    @ColumnInfo(name = "timestamp")
    private String timestamp; // ISO 8601 format

    @ColumnInfo(name = "sync_status")
    private String syncStatus; // e.g., "PENDING", "SYNCED", "FAILED"

}
