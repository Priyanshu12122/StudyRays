package com.xerox.studyrays.cacheDb.pwCache.batchDetailsDb

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        BatchDetailsEntityy::class,
        LessonsEntityy::class,
        DppNotesEntity::class,
        DppVideos::class,
        NotesEntityy::class,
        VideoEntityy::class,
        TimelineEntity::class
    ],
    version = 1
)
abstract class BatchDetailsDbb : RoomDatabase() {
    abstract val batchDetailsDao: BatchDetailsDaoo
    abstract val lessonsDao: LessonDao
    abstract val dppNotesDao: DppNotesDao
    abstract val notesDao: NotesDao
    abstract val videoDao: VideoDao
    abstract val dppVideoDao: DppVideoDao
    abstract val timelineDao: TimelineDao
}