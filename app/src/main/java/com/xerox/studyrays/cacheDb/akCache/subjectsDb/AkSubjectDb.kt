package com.xerox.studyrays.cacheDb.akCache.subjectsDb

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [AkSubjectEntity::class], version = 1, exportSchema = false)
abstract class AkSubjectDb: RoomDatabase() {
    abstract val dao: AkSubjectDao
}