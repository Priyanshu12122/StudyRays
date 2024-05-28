package com.xerox.studyrays.cacheDb.akCache.videosDb

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [AkVideoEntity::class], version = 1)
abstract class AkVideoDb: RoomDatabase() {
    abstract val dao: AkVideoDao
}