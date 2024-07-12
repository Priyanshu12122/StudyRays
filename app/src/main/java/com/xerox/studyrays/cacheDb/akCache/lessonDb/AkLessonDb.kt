package com.xerox.studyrays.cacheDb.akCache.lessonDb

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [AkLessonEntity::class], version = 1, exportSchema = false)
abstract class AkLessonDb: RoomDatabase() {
    abstract val dao: AkLessonDao
}