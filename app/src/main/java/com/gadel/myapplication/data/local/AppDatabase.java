package com.gadel.myapplication.data.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.gadel.myapplication.data.local.dao.ApprovalDetailDao;
import com.gadel.myapplication.data.local.dao.ApprovalTaskDao;
import com.gadel.myapplication.data.local.dao.RejectionReasonMasterDao;
import com.gadel.myapplication.data.local.dao.SyncActionDao;
import com.gadel.myapplication.data.local.dao.UserProfileDao;
import com.gadel.myapplication.data.local.entity.ApprovalDetail;
import com.gadel.myapplication.data.local.entity.ApprovalTask;
import com.gadel.myapplication.data.local.entity.RejectionReasonMaster;
import com.gadel.myapplication.data.local.entity.SyncAction;
import com.gadel.myapplication.data.local.entity.UserProfile;
import com.gadel.myapplication.utils.Converters;


// 1. CAMBIAMOS LA VERSIÓN A 2
@Database(entities = {ApprovalTask.class, ApprovalDetail.class,
        RejectionReasonMaster.class, SyncAction.class, UserProfile.class}, version = 3)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;

    public abstract ApprovalTaskDao approvalTaskDao();
    public abstract ApprovalDetailDao approvalDetailDao();
    public abstract RejectionReasonMasterDao rejectionReasonMasterDao();
    public abstract SyncActionDao syncActionDao();
    public abstract UserProfileDao userProfileDao();

    public static AppDatabase getDatabase(final Context context){
        if (INSTANCE == null){
            synchronized (AppDatabase.class){
                if (INSTANCE== null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "erp_mobile_db")
                            // 2. AGREGAMOS ESTA LÍNEA MÁGICA DE DESTRUCCIÓN SEGURA
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}