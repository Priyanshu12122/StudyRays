package com.xerox.studyrays.cacheDb.mainScreenCache.searchSectionDb

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [SearchEntity::class], version = 1, exportSchema = false)
abstract class SearchDb: RoomDatabase() {
    abstract val dao: SearchDao
}