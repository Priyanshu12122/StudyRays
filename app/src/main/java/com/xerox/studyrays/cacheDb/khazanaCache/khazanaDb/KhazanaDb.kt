package com.xerox.studyrays.cacheDb.khazanaCache.khazanaDb

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [KhazanaEntity::class], version = 1, exportSchema = false)
abstract class KhazanaDb: RoomDatabase() {
    abstract val dao: KhazanaDao
}