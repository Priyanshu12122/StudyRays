package com.xerox.studyrays.db.watchLaterDb

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [WatchLaterEntity::class], version = 1, exportSchema = false)
abstract class WatchLaterDb: RoomDatabase() {
    abstract val dao: WatchLaterDao
}