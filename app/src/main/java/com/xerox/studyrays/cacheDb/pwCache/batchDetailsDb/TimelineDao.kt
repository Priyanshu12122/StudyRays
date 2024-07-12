package com.xerox.studyrays.cacheDb.pwCache.batchDetailsDb

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface TimelineDao {

    @Upsert
    suspend fun insert(item: TimelineEntity)

    @Query("SELECT * FROM TimelineEntity WHERE externalId = :externalId")
    suspend fun getById(externalId: String): TimelineEntity?

    @Query("SELECT * FROM TimelineEntity")
    suspend fun getAll(): List<TimelineEntity>?

}