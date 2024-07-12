package com.xerox.studyrays.db.studyFocusDb

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface SubjectDao {

    @Upsert
    suspend fun upsertSubject(subject: SubjectsEntity)

    @Query("SELECT COUNT(*) FROM SubjectsEntity")
    fun getTotalSubjectCount(): Flow<Int>

    @Query("SELECT SUM(goalHours) FROM SubjectsEntity")
    fun getTotalGoalHours(): Flow<Float>

    @Query("SELECT * FROM SubjectsEntity WHERE subjectId = :subjectId")
    suspend fun getSubjectById(subjectId: Int): SubjectsEntity?

    @Query("DELETE FROM SubjectsEntity WHERE subjectId = :subjectId")
    suspend fun deleteSubject(subjectId: Int)

    @Query("SELECT * FROM SubjectsEntity")
    fun getAllSubjects(): Flow<List<SubjectsEntity>>
}