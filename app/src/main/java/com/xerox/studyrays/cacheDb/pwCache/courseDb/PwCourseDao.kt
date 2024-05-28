package com.xerox.studyrays.cacheDb.pwCache.courseDb

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface PwCourseDao {

    @Upsert
    suspend fun insert(item: PwCourseEntity)

    @Query("SELECT * FROM PwCourseEntity WHERE classValue = :classValue")
    suspend fun getById(classValue: String): List<PwCourseEntity>?
}