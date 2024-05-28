package com.xerox.studyrays.cacheDb.pwCache.videosDb

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface PwVideoDao {

    @Upsert
    suspend fun insert(item: PwVideoEntity)

    @Query("SELECT * FROM PwVideoEntity WHERE slug = :slug")
    suspend fun getById(slug: String): PwVideoEntity?
}
