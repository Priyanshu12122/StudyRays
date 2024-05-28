package com.xerox.studyrays.cacheDb.khazanaCache.khazanaNotesDb

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [KhazanaNotesEntity::class], version = 1)
abstract class KhazanaNotesDb: RoomDatabase() {
    abstract val dao: KhazanaNotesDao
}