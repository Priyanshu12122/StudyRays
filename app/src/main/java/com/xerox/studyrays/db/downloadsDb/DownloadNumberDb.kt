package com.xerox.studyrays.db.downloadsDb

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [DownloadNumberEntity::class], version = 1, exportSchema = false)
abstract class DownloadNumberDb: RoomDatabase() {
    abstract val dao: DownloadNumberDao
}