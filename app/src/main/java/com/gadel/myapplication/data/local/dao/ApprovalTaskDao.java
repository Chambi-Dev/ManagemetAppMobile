package com.gadel.myapplication.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;

@Dao
public interface ApprovalTaskDao {
    //@Insert(onConflict = OnConflictStrategy.REPLACE)
    //void insertApprovalTask(Approva task);
}
