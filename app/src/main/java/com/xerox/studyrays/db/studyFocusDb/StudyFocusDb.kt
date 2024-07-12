package com.xerox.studyrays.db.studyFocusDb

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [SubjectsEntity::class, SessionEntity::class, TaskEntityy::class],
    version = 1
)
@TypeConverters(ColorListConverter::class)
abstract class StudyFocusDb: RoomDatabase() {

    abstract fun subjectDao(): SubjectDao
    abstract fun taskDao(): TaskDaoo
    abstract fun sessionDao(): SessionDao
}