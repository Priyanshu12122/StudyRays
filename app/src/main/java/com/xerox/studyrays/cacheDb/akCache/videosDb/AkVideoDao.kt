package com.xerox.studyrays.cacheDb.akCache.videosDb

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface AkVideoDao {

    @Upsert
    suspend fun upsert(item: AkVideoEntity)

    @Query("SELECT * FROM akvideoentity WHERE tid = :tid")
    suspend fun getById(tid: Int): AkVideoEntity?
}