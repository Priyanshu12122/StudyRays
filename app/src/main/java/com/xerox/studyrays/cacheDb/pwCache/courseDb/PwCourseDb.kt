package com.xerox.studyrays.cacheDb.pwCache.courseDb

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [PwCourseEntity::class], version = 1)
abstract class PwCourseDb: RoomDatabase() {
    abstract val dao: PwCourseDao
}