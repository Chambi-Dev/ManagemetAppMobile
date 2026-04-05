package com.gadel.myapplication.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.Data;

@Data
@Entity(tableName = "rejection_reason_master")
public class RejectionReasonMaster {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "reason_id")
    private String reasonId;

    @ColumnInfo(name = "reason_desc")
    private String reasonDesc;

    @ColumnInfo(name = "company_id")
    private String companyId;


}
