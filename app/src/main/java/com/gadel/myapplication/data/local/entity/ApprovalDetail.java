package com.gadel.myapplication.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.Data;

@Data
@Entity(tableName = "approval_detail")
public class ApprovalDetail {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "detail_id")
    private String detailId;

    @ColumnInfo(name = "task_id")
    private String taskId;

    @ColumnInfo(name = "detail_item_no")
    private Integer detailItemNo;

    @ColumnInfo(name = "detail_decription")
    private String detailDescription;

    @ColumnInfo(name ="detail_quantity")
    private Double detailQuantity;

    @ColumnInfo(name = "detail_unit_price")
    private Double detailUnitPrice;



}
