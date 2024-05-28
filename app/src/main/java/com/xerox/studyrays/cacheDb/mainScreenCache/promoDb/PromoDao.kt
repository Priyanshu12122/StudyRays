package com.xerox.studyrays.cacheDb.mainScreenCache.promoDb

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface PromoDao {

    @Upsert
    suspend fun insertPromoEntity(item: PromoEntity)

    @Query("SELECT * FROM `Promo Cache Db`")
    suspend fun getAllItems(): List<PromoEntity>?

}