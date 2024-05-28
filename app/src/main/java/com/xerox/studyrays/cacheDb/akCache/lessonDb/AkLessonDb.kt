package com.xerox.studyrays.cacheDb.akCache.lessonDb

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [AkLessonEntity::class], version = 1)
abstract class AkLessonDb: RoomDatabase() {
    abstract val dao: AkLessonDao
}