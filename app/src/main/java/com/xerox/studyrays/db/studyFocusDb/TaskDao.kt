package com.xerox.studyrays.db.studyFocusDb

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDaoo {

    @Upsert
    suspend fun upsertTask(task: TaskEntityy)

    @Query("DELETE FROM TaskEntityy WHERE taskId = :taskId")
    suspend fun deleteTask(taskId: Int)

    @Query("DELETE FROM TaskEntityy WHERE taskSubjectId = :subjectId")
    suspend fun deleteTasksBySubjectId(subjectId: Int)

    @Query("SELECT * FROM TaskEntityy WHERE taskId = :taskId")
    suspend fun getTaskById(taskId: Int): TaskEntityy?

    @Query("SELECT * FROM TaskEntityy WHERE taskSubjectId = :subjectId")
    fun getTasksForSubject(subjectId: Int): Flow<List<TaskEntityy>>

    @Query("SELECT * FROM TaskEntityy")
    fun getAllTasks(): Flow<List<TaskEntityy>>
}