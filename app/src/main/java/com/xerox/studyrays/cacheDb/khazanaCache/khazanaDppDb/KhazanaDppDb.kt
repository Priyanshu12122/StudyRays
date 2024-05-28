package com.xerox.studyrays.cacheDb.khazanaCache.khazanaDppDb

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [KhazanaDppEntity::class], version = 1)
abstract class KhazanaDppDb: RoomDatabase() {
    abstract val dao: KhazanaDppDao
}