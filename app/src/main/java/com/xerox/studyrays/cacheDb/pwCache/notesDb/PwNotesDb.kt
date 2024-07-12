package com.xerox.studyrays.cacheDb.pwCache.notesDb

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [PwNotesEntity::class], version = 1, exportSchema = false)
abstract class PwNotesDb: RoomDatabase() {
    abstract val dao: PwNotesDao
}