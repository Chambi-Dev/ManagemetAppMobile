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
    public String userId;

    @ColumnInfo(name = "user_name")
    public String userName;

    @ColumnInfo(name = "user_email")
    public String userEmail;

    @ColumnInfo(name = "user_role")
    public String userRole;

    @ColumnInfo(name = "company_id")
    public String companyId;

    @ColumnInfo(name = "last_sync_date")
    public String lastSyncDate;



}
