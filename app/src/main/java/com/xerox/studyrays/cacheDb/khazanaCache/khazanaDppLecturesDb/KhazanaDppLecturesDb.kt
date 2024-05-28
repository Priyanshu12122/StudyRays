package com.xerox.studyrays.cacheDb.khazanaCache.khazanaDppLecturesDb

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [KhazanaDppLecturesEntity::class], version = 1)
abstract class KhazanaDppLecturesDb: RoomDatabase() {
    abstract val dao: KhazanaDppLecturesDao
}