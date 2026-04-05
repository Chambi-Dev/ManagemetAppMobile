package com.gadel.myapplication.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.gadel.myapplication.data.local.entity.UserProfile;

import java.util.List;

@Dao
public interface UserProfileDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUserProfile(UserProfile userProfile);

    @Query("SELECT *FROM user_profile")
    LiveData<List<UserProfile>> getUserProfile();
}
