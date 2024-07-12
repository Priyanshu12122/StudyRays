package com.xerox.studyrays.cacheDb.khazanaCache.khazanaTeachersDb

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [KhazanaTeachersEntity::class], version = 1, exportSchema = false)
abstract class KhazanaTeachersDb : RoomDatabase() {
    abstract val dao: KhazanaTeachersDao
}
