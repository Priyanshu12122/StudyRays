package com.xerox.studyrays.cacheDb.mainScreenCache.priceDb

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [PriceEntity::class], version = 1, exportSchema = false)
abstract class PriceDb: RoomDatabase() {
    abstract val dao: PriceDao
}