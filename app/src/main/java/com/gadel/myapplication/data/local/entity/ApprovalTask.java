package com.gadel.myapplication.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDateTime;

import lombok.Data;

@Data
@Entity(tableName = "approval_tasks")
public class ApprovalTask {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "task_id")
    private String taskId;

    @ColumnInfo(name = "backend_doc_id")
    private Long backendDocId;

    @ColumnInfo(name = "modulo_type")
    private String moduloType;

    @ColumnInfo(name = "requester_name")
    private String requesterName;

    @ColumnInfo(name = "total_amount")
    private Double totalAmount;

    @ColumnInfo(name = "summary_text")
    private String summaryText;

    @ColumnInfo(name = "request_date")
    private LocalDateTime requestDate;

    @ColumnInfo(name = "local_status")
    private String localStatus;

}
