package com.xerox.studyrays.db.videoDb

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Video::class], version = 1)
abstract class VideoDatabase: RoomDatabase() {
    abstract val dao: VideoDao
}