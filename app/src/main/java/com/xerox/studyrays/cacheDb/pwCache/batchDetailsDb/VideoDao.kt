package com.xerox.studyrays.cacheDb.pwCache.batchDetailsDb

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface VideoDao {

    @Upsert
    suspend fun insert(item: VideoEntityy)

    @Query("SELECT * FROM VideoEntityy WHERE topicSlug = :topicSlug")
    suspend fun getById(topicSlug: String): VideoEntityy?

    @Query("SELECT * FROM VideoEntityy")
    suspend fun getAll(): List<VideoEntityy>?

}