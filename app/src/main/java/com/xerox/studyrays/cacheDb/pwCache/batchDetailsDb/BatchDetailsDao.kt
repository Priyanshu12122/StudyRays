package com.xerox.studyrays.cacheDb.pwCache.batchDetailsDb

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface BatchDetailsDaoo {

    @Upsert
    suspend fun insert(item: BatchDetailsEntityy)

    @Query("SELECT * FROM BatchDetailsEntityy WHERE batchId = :batchId")
    suspend fun getById(batchId: String): BatchDetailsEntityy?

    @Query("SELECT * FROM BatchDetailsEntityy")
    suspend fun getAll(): List<BatchDetailsEntityy>?

}