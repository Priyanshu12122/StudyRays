package com.xerox.studyrays.cacheDb.pwCache.subjectsAndTeachersCache

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        BatchDetailsEntity::class,
        PreviewImageEntity::class,
        SubjectEntity::class,
        TeacherIdEntity::class,
        ImageIdEntity::class
    ],
    version = 1
    , exportSchema = false
)
abstract class BatchDetailsDb : RoomDatabase() {
    abstract val dao: BatchDetailsDao
}
