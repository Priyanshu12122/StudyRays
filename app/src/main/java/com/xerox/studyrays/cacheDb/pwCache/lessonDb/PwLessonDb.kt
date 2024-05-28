package com.xerox.studyrays.cacheDb.pwCache.lessonDb

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [PwLessonEntity::class], version = 1)
abstract class PwLessonDb: RoomDatabase() {
    abstract val dao: PwLessonDao
}