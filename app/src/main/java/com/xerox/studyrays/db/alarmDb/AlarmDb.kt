package com.xerox.studyrays.db.alarmDb

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [AlarmEntity::class], version = 1, exportSchema = false)
abstract class AlarmDb: RoomDatabase() {
    abstract val dao: AlarmDao
}