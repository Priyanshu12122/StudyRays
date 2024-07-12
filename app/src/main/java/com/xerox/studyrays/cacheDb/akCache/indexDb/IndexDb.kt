package com.xerox.studyrays.cacheDb.akCache.indexDb

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [IndexEntity::class], version = 1, exportSchema = false)
abstract class IndexDb : RoomDatabase() {
    abstract val dao: IndexDao
}
