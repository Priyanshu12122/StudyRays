package com.xerox.studyrays.cacheDb.khazanaCache.khazanaDppDb

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface KhazanaDppDao {
    @Upsert
    suspend fun insert(item: KhazanaDppEntity)

    @Query("SELECT * FROM khazanadppentity WHERE topicName = :topicName")
    suspend fun getById(topicName: String): KhazanaDppEntity?

    @Query("SELECT * FROM KhazanaDppEntity")
    suspend fun getAll(): List<KhazanaDppEntity>?

}