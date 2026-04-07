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

    public ApprovalTask() {}

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "task_id")
    public String taskId = "";

    @ColumnInfo(name = "backend_doc_id")
    public Long backendDocId;

    @ColumnInfo(name = "modulo_type")
    public String moduloType;

    @ColumnInfo(name = "requester_name")
    public String requesterName;

    @ColumnInfo(name = "total_amount")
    public Double totalAmount;

    @ColumnInfo(name = "summary_text")
    public String summaryText;

    @ColumnInfo(name = "request_date")
    public LocalDateTime requestDate;

    @ColumnInfo(name = "local_status")
    public String localStatus;

}
