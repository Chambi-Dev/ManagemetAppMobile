package com.gadel.myapplication.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.Data;

@Data
@Entity(tableName = "user_profile")
public class UserProfile {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "user_id")
    private String userId;

    @ColumnInfo(name = "user_name")
    private String userName;

    @ColumnInfo(name = "user_email")
    private String userEmail;

    @ColumnInfo(name = "user_role")
    private String userRole;

    @ColumnInfo(name = "company_id")
    private String companyId;

    @ColumnInfo(name = "last_sync_date")
    private String lastSyncDate;



}
