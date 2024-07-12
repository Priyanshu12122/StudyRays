package com.xerox.studyrays.cacheDb.pwCache.videosDb

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [PwVideoEntity::class], version = 1, exportSchema = false)
abstract class PwVideoDb: RoomDatabase() {
    abstract val dao: PwVideoDao
}