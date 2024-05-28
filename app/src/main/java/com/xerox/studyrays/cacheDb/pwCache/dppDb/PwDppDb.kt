package com.xerox.studyrays.cacheDb.pwCache.dppDb

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [PwDppEntity::class], version = 1)
abstract class PwDppDb: RoomDatabase() {
    abstract val dao: PwDppDao
}