package com.xerox.studyrays.cacheDb.khazanaCache.khazanaSubjectsDb

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [KhazanaSubjectEntity::class], version = 1, exportSchema = false)
abstract class KhazanaSubjectDb : RoomDatabase() {
    abstract val dao: KhazanaSubjectDao
}
