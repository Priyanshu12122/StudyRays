package com.xerox.studyrays.cacheDb.keyGeneratorCache

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface KeyGenerateDao {

    @Upsert
    suspend fun upsert(item: KeyGenerateEntity)

    @Delete
    suspend fun delete(item: KeyGenerateEntity)

    @Query("SELECT * FROM KeyGenerateEntity WHERE id= :id")
    suspend fun getById(id: Int): KeyGenerateEntity?
}