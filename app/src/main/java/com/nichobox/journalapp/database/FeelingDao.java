package com.nichobox.journalapp.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.Date;
import java.util.List;

@Dao
public interface FeelingDao {
    @Query("SELECT * FROM feelings ORDER BY post_date DESC")
    LiveData<List<FeelingEntry>> loadAllFeelings();

    @Insert
    void insertFeeling(FeelingEntry feelingEntry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateFeeling(FeelingEntry feelingEntry);

    @Delete
    void deleteFeeling(FeelingEntry feelingEntry);

    @Query("SELECT * FROM feelings WHERE id = :id")
    LiveData<FeelingEntry> loadFeelingById(int id);

    @Query("SELECT * FROM feelings WHERE post_date = :date")
    FeelingEntry loadFeelingByDate(Date date);
}

