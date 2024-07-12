package com.xerox.studyrays.db.keyDb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface KeyDao {

    @Upsert
    suspend fun upsert(item: KeyEntity)

    @Delete
    suspend fun delete(item: KeyEntity)

    @Query("SELECT * FROM KeyEntity WHERE id = :id")
    suspend fun getById(id: Int): KeyEntity?


}