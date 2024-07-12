package com.xerox.studyrays.cacheDb.mainScreenCache.promoDb

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [PromoEntity::class], version = 1, exportSchema = false)
abstract class PromoDb: RoomDatabase() {
    abstract val dao: PromoDao
}