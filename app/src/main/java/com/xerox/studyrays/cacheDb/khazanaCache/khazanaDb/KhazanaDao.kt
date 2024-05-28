package com.xerox.studyrays.cacheDb.khazanaCache.khazanaDb

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface KhazanaDao {

    @Upsert
    suspend fun insertKhazana(item: KhazanaEntity)

    @Query("SELECT * FROM KhazanaEntity")
    suspend fun getAllKhazanaFromDB(): List<KhazanaEntity>?

}