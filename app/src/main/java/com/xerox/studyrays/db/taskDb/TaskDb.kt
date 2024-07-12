package com.xerox.studyrays.db.taskDb

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [TaskEntity::class], version = 1, exportSchema = false)
abstract class TaskDb: RoomDatabase() {
    abstract val dao: TaskDao
}