package com.xerox.studyrays.cacheDb.khazanaCache.khazanaLecturesDb

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [KhazanaLecturesEntity::class], version = 1, exportSchema = false)
abstract class KhazanaLecturesDb: RoomDatabase() {
    abstract val dao: KhazanaLecturesDao
}