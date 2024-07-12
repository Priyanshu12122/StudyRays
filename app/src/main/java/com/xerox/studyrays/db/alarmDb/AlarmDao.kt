package com.xerox.studyrays.db.alarmDb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface AlarmDao {

    @Upsert
    suspend fun upsert(item: AlarmEntity)

    @Query("SELECT * FROM AlarmEntity WHERE ID = :id")
    suspend fun getById(id: Int): AlarmEntity?

    @Delete
    suspend fun delete(item: AlarmEntity)
}