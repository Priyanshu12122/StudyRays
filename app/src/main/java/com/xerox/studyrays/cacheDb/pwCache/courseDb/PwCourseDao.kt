package com.xerox.studyrays.cacheDb.pwCache.courseDb

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface PwCourseDao {

    @Upsert
    suspend fun insert(item: PwCourseEntity)

    @Query("SELECT * FROM PwCourseEntity WHERE queryClassValue = :classValue AND isOld = :isOld")
    suspend fun getById(classValue: String, isOld: Boolean): List<PwCourseEntity>?

    @Query("SELECT * FROM PwCourseEntity")
    suspend fun getAll(): List<PwCourseEntity>?
}