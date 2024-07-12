package com.xerox.studyrays.cacheDb.pwCache.batchDetailsDb

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface LessonDao {

    @Upsert
    suspend fun insert(item: LessonsEntityy)

    @Query("SELECT * FROM LessonsEntityy WHERE slug = :subjectSlug")
    suspend fun getById(subjectSlug: String): LessonsEntityy?

    @Query("SELECT * FROM LessonsEntityy")
    suspend fun getAll(): List<LessonsEntityy>?
}