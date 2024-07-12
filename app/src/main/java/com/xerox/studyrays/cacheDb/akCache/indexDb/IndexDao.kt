package com.xerox.studyrays.cacheDb.akCache.indexDb

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert


@Dao
interface IndexDao {
    @Upsert
    suspend fun insertIndex(index: IndexEntity)

    @Query("SELECT * FROM indexentity WHERE id = :id")
    suspend fun getIndexString(id: Int): IndexEntity?

    @Query("SELECT * FROM IndexEntity")
    suspend fun getAll(): List<IndexEntity>?
}
