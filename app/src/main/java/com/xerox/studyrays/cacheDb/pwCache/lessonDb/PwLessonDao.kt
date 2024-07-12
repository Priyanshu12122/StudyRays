package com.xerox.studyrays.cacheDb.pwCache.lessonDb

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface PwLessonDao {
    @Upsert
    suspend fun insert(item: PwLessonEntity)

    @Query("SELECT * FROM PwLessonEntity WHERE subjectId = :subjectId")
    suspend fun getById(subjectId: String): List<PwLessonEntity>?

    @Query("SELECT * FROM PwLessonEntity")
    suspend fun getAll(): List<PwLessonEntity>?
}
