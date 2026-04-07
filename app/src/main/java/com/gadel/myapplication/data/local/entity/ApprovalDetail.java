package com.gadel.myapplication.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(tableName = "approval_details")
public class ApprovalDetail {

    @PrimaryKey(autoGenerate = true)
    public int id; // ID interno para SQLite

    @NonNull
    @ColumnInfo(name = "task_id")
    public String taskId; // La "llave foránea" que lo une con la cabecera (Ej. MR310000022)

    @ColumnInfo(name = "material_no")
    public String materialNo;

    @ColumnInfo(name = "description")
    public String description;

    @ColumnInfo(name = "quantity")
    public Double quantity;

    @ColumnInfo(name = "unit_price")
    public Double unitPrice;
}