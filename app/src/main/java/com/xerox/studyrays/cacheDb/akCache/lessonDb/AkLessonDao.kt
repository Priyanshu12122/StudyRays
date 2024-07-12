package com.xerox.studyrays.cacheDb.akCache.lessonDb

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface AkLessonDao {
    @Upsert
    suspend fun upsert(item: AkLessonEntity)

    @Query("SELECT * FROM aklessonentity WHERE sid = :sid")
    suspend fun getById(sid: Int): AkLessonEntity?

    @Query("SELECT * FROM AkLessonEntity")
    suspend fun getAll(): List<AkLessonEntity>?
}