package com.xerox.studyrays.cacheDb.pwCache.batchDetailsDb

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface DppNotesDao {

    @Upsert
    suspend fun insert(item: DppNotesEntity)

    @Query("SELECT * FROM DppNotesEntity WHERE topicSlug = :topicSlug")
    suspend fun getById(topicSlug: String): DppNotesEntity?

    @Query("SELECT * FROM DppNotesEntity")
    suspend fun getAll(): List<DppNotesEntity>?

}