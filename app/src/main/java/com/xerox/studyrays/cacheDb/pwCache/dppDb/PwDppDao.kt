package com.xerox.studyrays.cacheDb.pwCache.dppDb

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface PwDppDao {

    @Upsert
    suspend fun insert(item: PwDppEntity)

    @Query("SELECT * FROM PwDppEntity WHERE slug = :slug")
    suspend fun getById(slug: String): List<PwDppEntity>?

    @Query("SELECT * FROM PwDppEntity")
    suspend fun getAll(): List<PwDppEntity>?
}