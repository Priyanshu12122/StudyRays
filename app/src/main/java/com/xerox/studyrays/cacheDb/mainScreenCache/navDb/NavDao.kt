package com.xerox.studyrays.cacheDb.mainScreenCache.navDb

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface NavDao {

    @Upsert
    suspend fun upsert(item: NavEntity)

    @Query("SELECT * FROM NavEntity WHERE ID = :id")
    suspend fun getById(id: Int): NavEntity?
}