package com.xerox.studyrays.cacheDb.pwCache.subjectsAndTeachersCache

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.xerox.studyrays.cacheDb.pwCache.subjectsAndTeachersCache.relations.BatchDetailsWithSubjects

@Dao
interface BatchDetailsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBatchDetails(batchDetails: BatchDetailsEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubjects(subjects: List<SubjectEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTeacherIds(teacherIds: List<TeacherIdEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImageIds(imageIds: List<ImageIdEntity>)

    @Transaction
    @Query("SELECT * FROM BatchDetailsEntity WHERE courseId = :batchDetailsId")
    suspend fun getBatchDetailsWithSubjects(batchDetailsId: String): BatchDetailsWithSubjects?

    @Query("SELECT * FROM batchdetailsentity")
    suspend fun getAll(): List<BatchDetailsWithSubjects>?
}
