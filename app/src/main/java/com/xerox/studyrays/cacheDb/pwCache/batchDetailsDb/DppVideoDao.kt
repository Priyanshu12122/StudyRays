package com.xerox.studyrays.cacheDb.pwCache.batchDetailsDb

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface DppVideoDao {

    @Upsert
    suspend fun insert(item: DppVideos)

    @Query("SELECT * FROM DppVideos WHERE topicSlug = :topicSlug")
    suspend fun getById(topicSlug: String): DppVideos?

    @Query("SELECT * FROM DppVideos")
    suspend fun getAll(): List<DppVideos>?

}