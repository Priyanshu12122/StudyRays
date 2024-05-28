package com.xerox.studyrays.cacheDb.khazanaCache.khazanaChaptersDb

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [KhazanaChaptersEntity::class], version = 1)
abstract class KhazanaChaptersDb: RoomDatabase() {
    abstract val dao: KhazanaChaptersDao
}