package com.xerox.studyrays.cacheDb.mainScreenCache.navDb

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [NavEntity::class], version = 1)
abstract class NavDb: RoomDatabase() {
    abstract val dao: NavDao
}