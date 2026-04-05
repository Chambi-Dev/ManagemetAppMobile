package com.gadel.myapplication.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.gadel.myapplication.data.local.entity.RejectionReasonMaster;

import java.util.List;

@Dao
public interface RejectionReasonMasterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRejectReason(RejectionReasonMaster reason);

    @Query("SELECT * FROM rejection_reason_master")
    LiveData<List<RejectionReasonMaster>> getAll();


}
