package com.xerox.studyrays.db.taskDb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface TaskDao {

    @Upsert
    suspend fun upsert(item: TaskEntity)

    @Delete
    suspend fun delete(item: TaskEntity)

    @Query("SELECT * FROM TaskEntity WHERE ID = :id")
    suspend fun getById(id: Int): TaskEntity?

}