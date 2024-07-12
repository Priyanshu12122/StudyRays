package com.xerox.studyrays.cacheDb.akCache.NotesDb

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [AkNotesEntity::class], version = 1, exportSchema = false)
abstract class AkNoteDb: RoomDatabase() {
    abstract val dao: AkNoteDao
}