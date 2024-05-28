package com.xerox.studyrays.cacheDb.mainScreenCache.priceDb

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface PriceDao {

    @Query("SELECT * FROM priceentity WHERE id = :id")
    suspend fun getResponse(id: Int): PriceEntity?

    @Upsert
    suspend fun insertResponse(response: PriceEntity)

}