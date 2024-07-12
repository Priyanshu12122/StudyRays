package com.xerox.studyrays.db.videoNotesDb

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [VideoNoteEntity::class], version = 1)
abstract class VideoNoteDb: RoomDatabase() {
    abstract val dao: VideoNoteDao
}